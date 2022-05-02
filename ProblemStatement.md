#### **Problem Statement**

A parking lot is an area where cars can be parked for a certain amount of time. A parking lot can have multiple floors with each floor having a different number of slots and each slot being suitable for different types of vehicles.

**Requirements**

Create a REST API application for the parking lot system with the following requirements.

The functions that the parking lot system can do:

    Create the parking lot.
    Add floors to the parking lot.
    Add a parking lot slot to any of the floors.
    Given a vehicle, it finds the first available slot, books it, creates a ticket, parks the vehicle, and finally returns the ticket.
    Unparks a vehicle given the ticket id.
    Displays the number of free slots per floor for a specific vehicle type.
    Displays all the free slots per floor for a specific vehicle type.
    Displays all the occupied slots per floor for a specific vehicle type.
    Details about the Vehicles:
        Every vehicle will have a type, registration number.
        Different Types of Vehicles:
            Car
            Bike
            Truck
    Details about the Parking Slots:
        Each type of slot can park a specific type of vehicle.
        No other vehicle should be allowed by the system.
    Finding the first available slot should be based on:
        The slot should be of the same type as the vehicle.
        The slot should be on the lowest possible floor in the parking lot.
        The slot should have the lowest possible slot number on the floor.
        Numbered serially from 1 to n for each floor where n is the number of parking slots on that floor.
    Details about the Parking Lot Floors:
        Numbered serially from 1 to n where n is the number of floors.
        Might contain one or more parking lot slots of different types.
    Details about the Tickets:
        The ticket id would be of the following format:
            <parking_lot_id>_<floor_no>_<slot_no>