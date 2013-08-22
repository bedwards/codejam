import java.io.FileWriter;
import java.util.Random;


public class GenerateFiles {

		public static void main(String[] args) throws Exception {
			System.out.println("Time Complexity!!");
			Random generator = new Random(19580427);
			final int highestPower = 7;
			for (int i = 1; i <= highestPower; i++) {
				final long rowCount = Math.round(Math.pow(10, i));
				FileWriter writer = new FileWriter(String.format("random_%s.txt", rowCount));
				for ( int j = 0; j < rowCount; j++) {
					writer.write(String.format("%s\n", generator.nextInt()));
				}
				writer.close();
			}
		}
}
