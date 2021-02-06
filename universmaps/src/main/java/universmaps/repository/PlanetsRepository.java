 package universmaps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import universmaps.model.Planets;

public interface PlanetsRepository extends JpaRepository<Planets, Long>{
	
	

}
