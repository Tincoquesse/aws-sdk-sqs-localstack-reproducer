import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest

@Testcontainers
internal class SqsTestResource : QuarkusTestResourceLifecycleManager {

    companion object {
        const val TEST_QUEUE_NAME = "webhooks-queue-test"
        private val localStackImage = DockerImageName.parse("localstack/localstack:2.3.2")

        @Container
        val localStack: LocalStackContainer = LocalStackContainer(localStackImage)
            .withServices(LocalStackContainer.Service.SQS)
            .withEnv("AWS_ACCESS_KEY_ID", "accesskey")
            .withEnv("AWS_SECRET_ACCESS_KEY", "secretkey")

        val sqsClient: SqsClient by lazy {
            SqsClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.SQS))
                .httpClient(UrlConnectionHttpClient.create())
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.accessKey, localStack.secretKey)
                    )
                )
                .region(Region.EU_CENTRAL_1)
                .build()
        }
    }

    override fun start(): MutableMap<String, String> {
        localStack.start()
        createQueue(TEST_QUEUE_NAME)

        return mutableMapOf()
    }

    private fun createQueue(queueName: String) {
        val createQueueRequest = CreateQueueRequest.builder().queueName(queueName).build()
        sqsClient.createQueue(createQueueRequest)
    }

    override fun stop() {
        localStack.stop()
    }
}
