
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.plugins.PluginConfigurator
import com.intellij.ide.starter.project.NoProject
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Test
import java.io.File

class PluginTest {
    @Test
    fun simpleTestWithoutProject() {
        Starter.newContext(
            testName = "testExample",
            testCase = TestCase(IdeProductProvider.IC,
                projectInfo = NoProject,
            ).withVersion("2024.3")
        ).apply {
            val pathToPlugin = System.getProperty("path.to.build.plugin")
            PluginConfigurator(this).installPluginFromFolder(File(pathToPlugin))
        }.runIdeWithDriver().useDriverAndCloseIde {
        }
    }
}