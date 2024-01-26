package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.daoimpl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.daoimpl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.daoimpl.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
	private final FilmDbStorage filmDbStorage;
	private final MpaDbStorage mpaDbStorage;
	private final UserDbStorage userDbStorage;
	private Film film;
	private Film secondFilm;
	private User user;
	private User secondUser;

	@BeforeEach
	void setUp() {

		film = Film.builder()
				.id(1)
				.name("Test")
				.description("test description")
				.releaseDate(LocalDate.now().minusYears(40))
				.duration(180)
				.genres(new HashSet<>())
				.mpa(mpaDbStorage.getRatingMpaById(1))
				.build();

		secondFilm = Film.builder()
				.id(1)
				.name("Test 2")
				.description("test description 2")
				.releaseDate(LocalDate.now().minusYears(40))
				.duration(180)
				.genres(new HashSet<>()).mpa(mpaDbStorage.getRatingMpaById(1))
				.build();

		user = User.builder()
				.id(1)
				.name("Eugene")
				.login("Buhanzaz")
				.email("Buhanza21@yandex.ru")
				.birthday(LocalDate.now().minusYears(30))
				.build();

		secondUser = User.builder()
				.id(2)
				.name("Eugene")
				.login("Buhanzaz")
				.email("Buhanza21@gmail.ru")
				.birthday(LocalDate.now().minusYears(30))
				.build();
	}

	@Test
	void getAllFilms_shouldConfirmThatTwoFilmsWasAddedAtList() {
		filmDbStorage.create(film);
		filmDbStorage.create(secondFilm);
		Collection<Film> films = filmDbStorage.getFilms();

		assertThat(films).hasSize(2);
	}

	@Test
	void createFilm_shouldConfirmThatFilmIdExists() {
		filmDbStorage.create(film);
		Film filmOptional = filmDbStorage.getFilm(1);

		assertEquals(filmOptional.getId(), 1);
	}

	@Test
	void getFilmById_shouldConfirmThatFilmIdExists() {
		filmDbStorage.create(film);

		assertEquals(filmDbStorage.getFilm(1).getId(),film.getId());
	}

	@Test
	public void getAllUsers_shouldConfirmThatTwoUsersWasAddedAtList() {
		userDbStorage.create(user);
		userDbStorage.create(secondUser);
		Collection<User> users = userDbStorage.getUsers();

		assertThat(users).contains(user);
		assertThat(users).contains(secondUser);
	}

	@Test
	public void createUser_shouldConfirmThatUserIdExists() {
		userDbStorage.create(user);
		User userOptional = userDbStorage.getUser(1);

		assertEquals(userOptional.getId(), 1);
	}

	@Test
	public void getUserById_shouldConfirmThatUserNameExists() {
		userDbStorage.create(user);
		User userOptional = userDbStorage.getUser(1);

		assertEquals(userOptional.getName(), "Eugene");
	}
}