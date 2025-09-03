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
package hu.icellmobilsoft.sampler.ts.sample.fileupload.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * {@link InputStream} implementation that generates random content in the given length.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
public class RandomInputStream extends InputStream {

    private final long maxLengthByte;

    private final Random random;

    private long length;

    /**
     * Constructor.
     *
     * @param lengthInByte
     *            the size of the {@link InputStream} in bytes
     */
    public RandomInputStream(long lengthInByte) {
        this.length = 0;
        this.maxLengthByte = lengthInByte;
        random = new Random();
    }

    @Override
    public int read() throws IOException {

        if (length > maxLengthByte) {
            return -1;
        }

        length++;
        return random.nextInt();
    }

}
