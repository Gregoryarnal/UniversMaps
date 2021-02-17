 package universmaps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import universmaps.dto.PlanetsDTO;
import universmaps.model.Planets;

public interface PlanetsRepository extends JpaRepository<Planets, String>{
	
	Planets findByName(String name);
	List<Planets> findByAffichage(Boolean affichage);
}
