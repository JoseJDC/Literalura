import Model.Autor;
import Model.Libro;
import Service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class AppApplication implements CommandLineRunner {

	@Autowired
	private LibraryService libraryService;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("1.- Buscar un libro por titulo y registrarlo");
			System.out.println("2.- Listar libros registrados");
			System.out.println("3.- Listar autores registrados");
			System.out.println("4.- Listar autores vivos en un determinado año");
			System.out.println("5.- Listar libros por idioma");
			System.out.println("0.- Salir");
			System.out.print("Elige una opción: ");
			int opcion = scanner.nextInt();
			scanner.nextLine(); // Consumir nueva línea

			switch (opcion) {
				case 1:
					System.out.print("Ingrese el título del libro: ");
					String titulo = scanner.nextLine();
					Libro libro = libraryService.buscarYRegistrarLibro(titulo);
					if (libro != null) {
						System.out.println("Libro registrado: " + libro.getTitulo());
					} else {
						System.out.println("No se encontró el libro.");
					}
					break;
				case 2:
					List<Libro> libros = libraryService.listarLibros();
					libros.forEach(l -> System.out.println(l.getTitulo()));
					break;
				case 3:
					List<Autor> autores = libraryService.listarAutores();
					autores.forEach(a -> System.out.println(a.getNombre()));
					break;
				case 4:
					System.out.print("Ingrese el año: ");
					int ano = scanner.nextInt();
					scanner.nextLine(); // Consumir nueva línea
					List<Autor> autoresVivos = libraryService.listarAutoresVivosEnAno(ano);
					autoresVivos.forEach(a -> System.out.println(a.getNombre()));
					break;
				case 5:
					System.out.print("Ingrese el idioma: ");
					String idioma = scanner.nextLine();
					List<Libro> librosPorIdioma = libraryService.listarLibrosPorIdioma(idioma);
					librosPorIdioma.forEach(l -> System.out.println(l.getTitulo()));
					break;
				case 0:
					System.out.println("Saliendo...");
					return;
				default:
					System.out.println("Opción no válida.");
					break;
			}
		}
	}

}
