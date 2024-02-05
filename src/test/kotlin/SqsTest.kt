import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(SqsTestResource::class)
class SqsTest {

    @Test
    fun `Test should fail when quarkus version is '3_7_1'`(){
        // empty
    }
}