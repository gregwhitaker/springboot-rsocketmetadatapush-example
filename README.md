# springboot-rsocketmetadatapush-example
An example of using Metadata Push with [RSocket](http://rsocket.io) and Spring Boot.

This example consists of an RSocket service, `hello-service`, that returns hello messages in the format specified during connection
setup or by the format specified as an update to the metadata via metadata push.

## Building the Example
Run the following command to build the example:

    ./gradlew clean build
    
## Running the Example
Follow the steps below to run the example:

1. Start the `hello-service` by running the following command:

        ./gradlew :hello-service:bootRun
        
2. In a new terminal, run the following command to start the `hello-client`:

        ./gradlew :hello-client:bootRun --args="Bob"
        
    If successful, you will see the following in the terminal:

        : Started HelloClientApplication in 0.967 seconds (JVM running for 1.336)
        : Sending hello request 1...
        : Response: Hello, Bob! [clientId: 'greg_demo_client']
        : Pushing new hello message format via METADATA_PUSH: Bonjour, %s!
        : Sending hello request 2...
        : Response: Bonjour, Bob! [clientId: 'greg_demo_client']
        
    Notice that the first hello message uses the format `Hello, {name}!`. The message format is then updated to `Bonjour, {name}!` 
    via RSocket metadata push and a new request is sent through to show that the message has been updated.

    Also, notice that the `clientId` metadata in both scenarios remains as it was set during the RSocket connection setup.
    
## Bugs and Feedback
For bugs, questions, and discussions please use the [Github Issues](https://github.com/gregwhitaker/springboot-rsocketmetadatapush-example/issues).

## License
MIT License

Copyright (c) 2019 Greg Whitaker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.