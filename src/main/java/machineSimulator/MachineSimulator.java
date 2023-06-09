package machineSimulator;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.regex.Pattern;

public class MachineSimulator {

    private static MqttHandlerMachine mqttHandler;

    private static String machineNumber;

    private static final Pattern patternTaxiNumber = Pattern.compile("[АВЕКМНОРСТУХ]{2}\\d{3}(?<!000)\\d{2,3}", Pattern.MULTILINE);

    private static final Pattern patternMachineNumber = Pattern.compile("[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}", Pattern.MULTILINE);

    public static String getMachineNumber() {
        return machineNumber;
    }

    public static void setMachineNumber(String machineNumber) {
        MachineSimulator.machineNumber = machineNumber;
    }

    public static void main(String[] args) {

        if (patternTaxiNumber.matcher(System.getenv("CAR_NUMBER")).find() || patternMachineNumber.matcher(System.getenv("CAR_NUMBER")).find()){
            machineNumber = System.getenv("CAR_NUMBER");
        } else {
            System.err.println("Machine number is incorrect.");
            System.exit(1);
        }

        mqttHandler = new MqttHandlerMachine(machineNumber);
        try {
            mqttHandler.sendData();
        } catch (MqttException e) {
            System.err.println("Can't publish message to channel.");
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Thread exception.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOError. " + e.getMessage());
            System.exit(1);
        }
    }
}
