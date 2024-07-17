package Repository;

import Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findByFechaDefuncionIsNull();
    Autor findByNombre(String authorName);
}
