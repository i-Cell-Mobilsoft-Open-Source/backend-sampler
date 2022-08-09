db.createUser({
    user: "sample_user",
    pwd: "sample_pass",
    roles: [{role: "readWrite", db: "sample_db"}]
});
