package services;

import com.parking.apiparking.entities.Car;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private List<Car> parkinglot;
    private static final double HOURLY_RATE = 2.5;

    public ParkingService(){
        this.parkinglot = new ArrayList<>();
    }

    /*TODO: US 01: Como usuario, quiero poder ver la lista de todos los autos estacionados en
    el parqueo, para poder tener una visión general del estado actual del estacionamiento.
    */
    public List<Car> getAllCars(){
        return this.parkinglot;
    }

    public Optional<Car> getCarByLicensePlate(String licensePlate){
        return this.parkinglot.stream().filter(car -> car.getLicensePlate().equals(licensePlate)).findFirst();
    }
    public List<Car> getCarsByColor(String color){
        return this.parkinglot.stream().filter(car -> car.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
    }
    /*TODO: US 02: Como usuario, quiero poder agregar un auto al parqueo,
    para poder estacionar mi vehiculo.
     */
    public void addCar(Car car){
        this.parkinglot.add(car);
    }

    public boolean removeCarByLicensePlate(String licensePlate){
        return this.parkinglot.removeIf(car -> car.getLicensePlate().equals(licensePlate));
    }
    public void parkCar(Car car){
        car.setEntryTime(LocalDateTime.now());
        addCar(car);
    }
    public void unpackCar(String licensePlate){
        removeCarByLicensePlate(licensePlate);
    }
    public double calculateParkingFeee(String licensePlate){
        Optional<Car> optionalCar = getCarByLicensePlate(licensePlate);
        if(optionalCar.isPresent()){
            Car car = optionalCar.get();
            LocalDateTime entryTime = car.getEntryTime();
            if(entryTime != null){
                Long hoursParked = ChronoUnit.HOURS.between(entryTime, LocalDateTime.now());
                return hoursParked * HOURLY_RATE;
            }
        }
        return 0;
    }
}