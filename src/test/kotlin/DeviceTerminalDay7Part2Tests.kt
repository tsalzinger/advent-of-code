import me.salzinger.common.FileType
import me.salzinger.common.getFile
import org.junit.jupiter.api.Test
import puzzles.Puzzle07NoSpaceLeftOnDevice.Part2.solve

class DeviceTerminalDay7Part2Tests {
    @Test
    fun testDirectorySizeSum() {
        getFile(level = 7, part = "example-1", FileType.IN)
            .readLines()
            .solve()
            .toString()
            .assertThat {
                isEqualTo(getFile(level = 7, part = "2-example-1", FileType.SOLUTION).readLines().first())
            }
    }
}

