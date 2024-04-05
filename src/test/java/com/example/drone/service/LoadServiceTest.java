package com.example.drone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.drone.model.viewmodel.MedicineVm;
import com.example.drone.repository.BatteryCapacityRepository;
import com.example.drone.repository.DroneRepository;
import com.example.drone.repository.LoadMedicationRepository;
import com.example.drone.repository.LoadRepository;
import com.example.drone.repository.MedicationRepository;

//import lombok.var;

import com.example.drone.model.persistent.Medication;


@ExtendWith(MockitoExtension.class)
public class LoadServiceTest {
	
	  @Mock
	  private DroneRepository droneRepository;
	  
	  @Mock
	  private LoadRepository loadRepository;
	  
	  @Mock
	  private LoadMedicationRepository loadMedicationRepository;
	  
	  @Mock
	  private MedicationRepository medicationRepository;
	  
	  @Mock
	  private BatteryCapacityRepository batteryCapacityRepository;
	  
	  @InjectMocks
	  private LoadService loadService;
	  
	  @Test
	  public void testCalculateTotalweightWhenNoMedicine() {
		  List<MedicineVm> medicines  = new ArrayList<MedicineVm>(); 
		  assertEquals(0, loadService.calculateTotalweight(medicines));
	  }
	  @Test
	  public void testCalculateTotalweightWithOneMedicines() {
		  var medicines  = new ArrayList<MedicineVm>(); 
		  var medicineVm  = MedicineVm.builder().medicineId(1).quantity(3).build();
		  medicines.add(medicineVm);
		  var medication = Medication.builder().weight(50).build();
		  Optional<Medication> medicationOpt =  Optional.of(medication);
		  when(medicationRepository.findById(1)).thenReturn(medicationOpt);
		  assertEquals(150, loadService.calculateTotalweight(medicines));
	  }
}
