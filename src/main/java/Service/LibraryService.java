package Service;

import Model.Autor;
import Model.Libro;
import Repository.AutorRepository;
import Repository.LibroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class LibraryService {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private static final String GUTENDEX_API_URL = "https://gutendex.com/books?search=";

    public Libro buscarYRegistrarLibro(String titulo) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GUTENDEX_API_URL + titulo;
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response);
            JsonNode bookNode = root.path("results").get(0);
            String bookTitle = bookNode.path("title").asText();
            String language = bookNode.path("languages").get(0).asText();
            Date publishDate = new Date(); // Use a real date parser if available

            // Assuming the author data is in the same JSON response
            String authorName = bookNode.path("authors").get(0).path("name").asText();
            Autor autor = autorRepository.findByNombre(authorName);
            if (autor == null) {
                autor = new Autor();
                autor.setNombre(authorName);
                // Set other author details
                autorRepository.save(autor);
            }

            Libro libro = new Libro();
            libro.setTitulo(bookTitle);
            libro.setIdioma(language);
            libro.setFechaPublicacion(publishDate);
            libro.setAutor(autor);

            return libroRepository.save(libro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEnAno(int ano) {
        return autorRepository.findByFechaDefuncionIsNull();
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }
}
