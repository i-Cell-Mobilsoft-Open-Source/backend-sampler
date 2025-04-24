/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.ts.sample.consumer.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.awaitility.Awaitility;
import org.awaitility.pollinterval.FibonacciPollInterval;
import org.hamcrest.number.OrderingComparison;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.module.redisstream.annotation.RedisStreamProducer;
import hu.icellmobilsoft.coffee.module.redisstream.config.StreamMessageParameter;
import hu.icellmobilsoft.coffee.module.redisstream.publisher.RedisStreamPublisher;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import hu.icellmobilsoft.coffee.se.util.string.RandomUtil;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.sampler.sample.comsumer.model.CarProduction;
import hu.icellmobilsoft.sampler.sample.comsumer.model.constant.RedisStreamConstant;
import hu.icellmobilsoft.sampler.sample.comsumer.model.enums.ProductionStatus;
import hu.icellmobilsoft.sampler.sample.comsumer.model.process.ProcessCarPaintedToAssembled;
import hu.icellmobilsoft.sampler.sample.comsumer.model.process.ProcessCarWeldedToPainted;

/**
 * IT tests for {@code PainterStreamConsumer}.
 * </p>
 * Prerequisities:
 *
 * <pre>
 * SAMPLER_ROOT=/home/user/git/backend-sampler
 * VERSION=2.0.0-SNAPSHOT
 *
 * docker-compose -f ${SAMPLER_ROOT}/etc/docker-compose/docker-compose.local.observability.yml up -d
 * docker-compose -f ${SAMPLER_ROOT}/etc/docker-compose/docker-compose.local.postgres.yml up -d
 * docker-compose -f ${SAMPLER_ROOT}/etc/docker-compose/docker-compose.local.redis.yml up -d
 * docker-compose -f ${SAMPLER_ROOT}/etc/docker-compose/docker-compose.local.wildfly-postgresql.yml up -d --force-recreate
 * docker cp ${SAMPLER_ROOT}/sample/sample-consumer/sample-consumer-service/target/sample-consumer-service-${VERSION}.war bs-sample-service:/home/icellmobilsoft/wildfly/standalone/deployments/ROOT.war
 * </pre>
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@DisplayName("Testing PainterStreamConsumer")
@Tag(TestSuiteGroup.RESTASSURED)
class PainterStreamConsumerIT extends BaseConfigurableWeldIT {

    @Inject
    private EntityManager em;

    @Inject
    @RedisStreamProducer(configKey = RedisStreamConstant.CarProduction.Painter.CONFIG_KEY,
            group = RedisStreamConstant.CarProduction.Painter.GROUP)
    protected RedisStreamPublisher painterRedisStreamPublisher;

    private String carProductionId;

    @BeforeEach
    void beforeEach() {
        MDC.put(LogConstants.LOG_SESSION_ID, "test-" + RandomUtil.generateId());
    }

    @Test
    void testCarProductionWeldedToPainted() throws BaseException {

        // given
        carProductionId = RandomUtil.generateId();

        CarProduction carProduction = new CarProduction();
        carProduction.setId(carProductionId);
        carProduction.setProductionStatus(ProductionStatus.WELDED);

        ProcessCarWeldedToPainted processCarWeldedToPainted = new ProcessCarWeldedToPainted();
        processCarWeldedToPainted.setCarProduction(carProduction);
        processCarWeldedToPainted.setStreamMessage(carProduction.getId());

        em.getTransaction().begin();
        carProduction = em.merge(carProduction);
        processCarWeldedToPainted = em.merge(processCarWeldedToPainted);
        em.getTransaction().commit();

        String streamMessage = processCarWeldedToPainted.getStreamMessage();

        // when
        painterRedisStreamPublisher.publish(
                streamMessage,
                Map.ofEntries(
                        RedisStreamPublisher.parameterOf(StreamMessageParameter.TTL, Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli())));

        // then
        // wait for the consumer to update the CarProduction entity
        Awaitility //
                .waitAtMost(10, TimeUnit.SECONDS) //
                .pollInterval(FibonacciPollInterval.fibonacci(TimeUnit.SECONDS)) //
                .until(() -> {
                    em.clear();
                    return em.createQuery("SELECT c.version FROM CarProduction c WHERE c.id = :carProductionId", Long.class)
                            .setParameter("carProductionId", carProductionId)
                            .getResultList()
                            .stream()
                            .findFirst()
                            .orElse(-1L);
                }, OrderingComparison.greaterThan(carProduction.getVersion()));

        // assert updated fields
        carProduction = em.find(CarProduction.class, carProductionId);
        Assertions.assertEquals(ProductionStatus.PAINTED, carProduction.getProductionStatus());
        Assertions.assertNotNull(carProduction.getLayers());
        Assertions.assertNotNull(carProduction.getColor());

        // assert process records
        Assertions.assertNull(em.find(ProcessCarWeldedToPainted.class, processCarWeldedToPainted.getId()));
        Assertions.assertEquals(
                1,
                em.createQuery(
                        "SELECT p FROM ProcessCarPaintedToAssembled p WHERE p.carProduction.id = :carProductionId",
                        ProcessCarPaintedToAssembled.class)
                        .setParameter("carProductionId", carProductionId)
                        .getResultList()
                        .size());
    }

    @AfterEach
    void cleanup() {

        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        if (Objects.isNull(carProductionId)) {
            return;
        }

        em.getTransaction().begin();

        em.createQuery("DELETE ProcessCarWeldedToPainted p WHERE p.carProduction.id = :carProductionId")
                .setParameter("carProductionId", carProductionId)
                .executeUpdate();
        em.createQuery("DELETE ProcessCarPaintedToAssembled p WHERE p.carProduction.id = :carProductionId")
                .setParameter("carProductionId", carProductionId)
                .executeUpdate();
        em.createQuery("DELETE CarProduction c WHERE c.id = :carProductionId") //
                .setParameter("carProductionId", carProductionId)
                .executeUpdate();

        em.getTransaction().commit();
    }

}
