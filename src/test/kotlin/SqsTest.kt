import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(SqsTestResource::class)
class SqsTest {

    @Test
    fun `Should succeed when quarkus version is'3_5_3'`(){
        // empty
    }
}