package javasmmr.zoowsome.controllers;

import javasmmr.zoowsome.services.factories.*;
import javasmmr.zoowsome.services.factories.animalFactories.*;
import javasmmr.zoowsome.models.animals.*;
import javasmmr.zoowsome.models.employees.*;
import javasmmr.zoowsome.services.factories.employeeFactories.*;

import java.util.ArrayList;
import java.util.Random;
import javasmmr.zoowsome.repositories.*;

public class MainController {

	public static final int NR_OF_SPECIES = 10;
	public static final int NR_OF_ANIMALS = 10;
	public static final int NR_OF_EMPLOYEES = 20;
	
	public static void main(String[] args)
	{
		Random randNr = new Random();
		
		try{
			AnimalFactory abstractFactory = new AnimalFactory();
			
			SpeciesFactory[] speciesFactoryArray = new SpeciesFactory[NR_OF_SPECIES];
			Animal[] animalArray = new Animal[NR_OF_ANIMALS];
			
			//we will get NR_OF_SPECIES random species, e.g., Mammals, Reptiles, etc.
			for(int i = 0; i < NR_OF_SPECIES; ++i){
				speciesFactoryArray[i] = abstractFactory.getSpeciesFactory(randomSpecies(randNr));
			}
			
			//we will get a random animal for each of the NR_OF_ANIMALS species we got above
			for(int i = 0; i < NR_OF_ANIMALS; i++){
				animalArray[i] = speciesFactoryArray[i].getAnimal(randomAnimalFromOneSpecies(randNr, speciesFactoryArray[i]));
			}
			
			//printing all animals created
			int i = 0;
			
			/*for(Animal x: animalArray){
			    System.out.println("Animal " + i++ + ": name = " + x.getName() + "; maintenanceCost = " + x.getMaintenanceCost() + 
						          " hours/week; takenCareOf = " + x.getTakenCareOf());
  		    }*/
			
			CareTakerFactory careTakerFactory = new CareTakerFactory();
			Employee[] caretakerArray = new Employee[NR_OF_EMPLOYEES];
			
			for(i = 0; i < NR_OF_EMPLOYEES; i++){
				caretakerArray[i] = careTakerFactory.getEmployeeFactory(Constants.Employees.CARETAKER);
			}
			
			for(Employee c: caretakerArray)
				for(Animal a: animalArray){
					
					if(!(c.getIsDead() || a.getTakenCareOf())){
						
						String result = ((CareTaker)c).takeCareOf(a);//casting an Employee reference to CareTaker, in order to access the method takeCareOf()
					   
						if(result.equals(Constants.Employees.Caretakers.TCO_KILLED)){
					    	
							c.setIsDead(true);
					    }
					    else if(result.equals(Constants.Employees.Caretakers.TCO_SUCCESS)){
					    	
					    	a.setTakenCareOf(true);
					    }
					    //else continue
					}
				}
			
			i = 0;
			
			for(Animal x: animalArray){
				System.out.println("Animal " + i++ + ": name = " + x.getName() + ";\t maintenanceCost = " + x.getMaintenanceCost() + 
				          " hours/week;\t takenCareOf = " + x.getTakenCareOf());
			}
			
			for(Employee c: caretakerArray){
				System.out.println(c.getName() + " " + ((CareTaker)c).getWorkingHours() + " dead = " + c.getIsDead() + " id: " + c.getId());
			}
			
			//XML part
			
			AnimalRepository animalRepositoryObj = new AnimalRepository();
			
			ArrayList<Animal> animalArrList1 = new ArrayList<Animal>();
			
			for(i = 0; i < NR_OF_ANIMALS; i++){
				animalArrList1.add(animalArray[i]);
			}
			
			//saving the animals from animalArray to an XML file
			animalRepositoryObj.save(animalArrList1);
			
			ArrayList<Animal> animalArrList2 = new ArrayList<Animal>();
			
			//loading animals in animalArrList2
			animalArrList2 = animalRepositoryObj.load();
			
			//printing the loaded animals from the XML file to see if they are correct
			System.out.println(animalArrList2.toString());
			
		}
		catch (Exception e){
			System.out.println(e);
		}
		
	}
	
	
	
	public static String randomSpecies(Random randNr){
		int nr = randNr.nextInt(5);
		
		switch(nr){
		case 0: return "MAMMALS";
		case 1: return "REPTILES";
		case 2: return "INSECTS";
		case 3: return "BIRDS";
		default: return "AQUATICS";
		}
	}
	
	public static String randomAnimalFromOneSpecies(Random randNr, SpeciesFactory aSpeciesFactory){
		int nr = randNr.nextInt(3);
		String speciesToString = aSpeciesFactory.toString(); //we will get from here something like: javasmmr.zoowsome.services.factories.animalFactories.BirdFactory@6d06d69c
		//and I have to extract that "Bird" from the string in order to know with what species we are dealing with
		
		char firstLetterNameOfSpecies = speciesToString.charAt(53);//the first letter of the species name
		
		switch(firstLetterNameOfSpecies){
		case 'B': switch(nr){              //we have to choose a Bird animal
		          case 0: return Constants.Animals.Birds.DUCK;
		          case 1: return Constants.Animals.Birds.EAGLE;
		          default: return Constants.Animals.Birds.SWAN;
		          }
		          
		case 'I':switch(nr){              //we have to choose an Insect animal
                 case 0: return Constants.Animals.Insects.BUTTERFLY;
                 case 1: return Constants.Animals.Insects.COCKROACH;
                 default: return Constants.Animals.Insects.SPIDER;
                 }
                
		case 'A':switch(nr){              //we have to choose an Aquatic animal
                 case 0: return Constants.Animals.Aquatics.FISH;
                 case 1: return Constants.Animals.Aquatics.SHARK;
                 default: return Constants.Animals.Aquatics.OCTOPUS;
                 }
		
		case 'R':switch(nr){              //we have to choose a Reptile animal
                 case 0: return Constants.Animals.Reptiles.CROCODILE;
                 case 1: return Constants.Animals.Reptiles.LIZARD;
                 default: return Constants.Animals.Reptiles.SNAKE;
                 }
		
		default:switch(nr){              //we have to choose a Mammal animal
                case 0: return Constants.Animals.Mammals.COW;
                case 1: return Constants.Animals.Mammals.MONKEY;
                default: return Constants.Animals.Mammals.TIGER;
               }
		}
		
	}
	
}
