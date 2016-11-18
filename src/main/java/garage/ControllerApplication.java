package garage;

import garage.lights.BlueToothDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ControllerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ControllerApplication.class, args);
		BlueToothDiscovery.getBluetoothList();
	}


}
