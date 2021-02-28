package root.entity_repository_services;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.entites.Room;
import root.entity_repositories.RoomRepository;

@Service
public class RoomService {
	@Autowired
	private RoomRepository roomRepository;
	
	public Room saveRoom(Room room) {
		return this.roomRepository.save(room);
	}
	
	public Optional<Set<Room>> findAllOfSingleRoomsOfCommunicator(String communicatorEmail) {
		return this.roomRepository.findAllOfSingleRoomsOfCommunicator(communicatorEmail);
	}
	
	public Optional<Room> findRoomById(long roomId) {
		return this.roomRepository.findById(roomId);
	}

	public Room updateRoom(Room room) {
		return this.roomRepository.save(room);
	}
}
