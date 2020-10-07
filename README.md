# ycf
Gradle plugin for Yandex Cloud Functions


## Configuration
Full configuration example with comments
```groovy
plugins {
    id 'java'
    id 'ru.mak_sim.ycf' version '0.5'
}

ycf {
    // Required parameter
    token = "${yc_oauth_token}" // OAuth token
    function_id = "d4e1antsi3gvon0hn7kv" // ID of the function to create a version for
    entrypoint = "ru.mak_sim.ya_func.Handler" // Entrypoint of the version.

    // Parameters with default values
    memory = 134217728 // Amount of memory available to the version, specified in bytes
    runtime = "java11" // Runtime environment for the version
    
    // Optional parameters with examples
    execution_timeout = 30 // Timeout for the execution of the version, specified in seconds
    environment = [key1: "1", key2: "2"] // Environment settings for the version
    tags = ["iddqd","idkfa"] // Function version tags
}
```

## Tasks
* `ycfList` – List yandex cloud functions, for all clouds available for specified OAuth token.
* `ycfCreateVersion` - Create new yandex cloud function version.
