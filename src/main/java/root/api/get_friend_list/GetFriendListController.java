package root.api.get_friend_list;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import root.entites.Communicator;
import root.entity_repository_services.CommunicatorService;
import root.utils.communicators.CommunicatorDTO;

@RestController
public class GetFriendListController {
	@Autowired
	private CommunicatorService communicatorService;
	
	@GetMapping("/get-friend-list/{communicator-email}")
	public List<CommunicatorDTO> getFriendList(@PathVariable("communicator-email") String communicatorEmail) {
		List<CommunicatorDTO> friendListDTO = new ArrayList<CommunicatorDTO>();
		Optional<Set<Communicator>> result = this.communicatorService.findFriendsOfCommunicator(communicatorEmail);
		if(!result.isEmpty()) {
			Set<Communicator> friendList = result.get();
			for(Communicator ele : friendList) {
				friendListDTO.add(new CommunicatorDTO(ele));
			}
		}
		return friendListDTO;
	}

}





































