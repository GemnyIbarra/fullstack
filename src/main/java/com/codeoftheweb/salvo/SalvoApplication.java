package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository,
									  GameRepository gRepository,
									  GamePlayerRepository gpRepository,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of customers
			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
			repository.save(player1);
			Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
			repository.save(player2);
			Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			repository.save(player3);
			Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole"));
			repository.save(player4);

			Date dateT = new Date();
			Game game1 = new Game(dateT);
			gRepository.save(game1);

			Date dateT2 = Date.from(dateT.toInstant().plusSeconds(3600));
			Game game2 = new Game(dateT2);
			gRepository.save(game2);

			Date dateT3 = Date.from(dateT.toInstant().plusSeconds(7200));
			Game game3 = new Game(dateT3);
			gRepository.save(game3);

			Date dateT4 = Date.from(dateT.toInstant().plusSeconds(10800));
			Game game4 = new Game(dateT4);
			gRepository.save(game4);

			Date dateT5 = Date.from(dateT.toInstant().plusSeconds(14400));
			Game game5 = new Game(dateT5);
			gRepository.save(game5);

			Date dateT6 = Date.from(dateT.toInstant().plusSeconds(18000));
			Game game6 = new Game(dateT6);
			gRepository.save(game6);

			Date dateT7 = Date.from(dateT.toInstant().plusSeconds(21600));
			Game game7 = new Game(dateT7);
			gRepository.save(game7);

			Date dateT8 = Date.from(dateT.toInstant().plusSeconds(25200));
			Game game8 = new Game(dateT8);
			gRepository.save(game8);

			GamePlayer gp1 = new GamePlayer(game1, player1);
			gpRepository.save(gp1);
			GamePlayer gp2 = new GamePlayer(game1, player2);
			gpRepository.save(gp2);

			GamePlayer gp3 = new GamePlayer(game2, player1);
			gpRepository.save(gp3);
			GamePlayer gp4 = new GamePlayer(game2, player2);
			gpRepository.save(gp4);

			GamePlayer gp5 = new GamePlayer(game3, player2);
			gpRepository.save(gp5);
			GamePlayer gp6 = new GamePlayer(game3, player4);
			gpRepository.save(gp6);

			GamePlayer gp7 = new GamePlayer(game4, player2);
			gpRepository.save(gp7);
			GamePlayer gp8 = new GamePlayer(game4, player1);
			gpRepository.save(gp8);

			GamePlayer gp9 = new GamePlayer(game5, player4);
			gpRepository.save(gp9);
			GamePlayer gp10 = new GamePlayer(game5, player1);
			gpRepository.save(gp10);

			GamePlayer gp11 = new GamePlayer(game6, player3);
			gpRepository.save(gp11);

			GamePlayer gp12 = new GamePlayer(game7, player4);
			gpRepository.save(gp12);

			GamePlayer gp13 = new GamePlayer(game8, player3);
			gpRepository.save(gp13);

			GamePlayer gp14 = new GamePlayer(game8, player4);
			gpRepository.save(gp14);

			String carrier = "Carrier";
			String battleship = "Battleship";
			String submarine = "Submarine";
			String destroyer = "Destroyer";
			String patrolBoat = "Patrol Boat";

			Ship ship1 = new Ship(gp1, destroyer, Arrays.asList("H2", "H3", "H4"));
			Ship ship2 = new Ship(gp1, submarine, Arrays.asList("E1", "F1", "G1"));
			Ship ship3 = new Ship(gp1, patrolBoat, Arrays.asList("B4", "B5"));
			Ship ship4 = new Ship(gp2, destroyer, Arrays.asList("B5", "C5", "D5"));
			Ship ship5 = new Ship(gp2, patrolBoat, Arrays.asList("F1", "F2"));

			Ship ship6 = new Ship(gp3, destroyer, Arrays.asList("B5", "C5", "D5"));
			Ship ship7 = new Ship(gp3, patrolBoat, Arrays.asList("C6", "C7"));
			Ship ship8 = new Ship(gp4, submarine, Arrays.asList("A2", "A3", "A4"));
			Ship ship9 = new Ship(gp4, patrolBoat, Arrays.asList("G6", "H6"));

			Ship ship10 = new Ship(gp5, destroyer, Arrays.asList("B5", "C5", "D5"));
			Ship ship11 = new Ship(gp5, patrolBoat, Arrays.asList("C6", "C7"));
			Ship ship12 = new Ship(gp6, submarine, Arrays.asList("A2", "A3", "A4"));
			Ship ship13 = new Ship(gp6, patrolBoat, Arrays.asList("G6", "H6"));

			Ship ship14 = new Ship(gp7, destroyer, Arrays.asList("B5", "C%", "D5"));
			Ship ship15 = new Ship(gp7, patrolBoat, Arrays.asList("C6", "C7"));
			Ship ship16 = new Ship(gp8, submarine, Arrays.asList("A2","A3", "A4"));
			Ship ship17 = new Ship(gp8, patrolBoat, Arrays.asList("G6", "H6"));

			Ship ship18 = new Ship(gp9, destroyer, Arrays.asList("B5", "C5", "D5"));
			Ship ship19 = new Ship(gp9, patrolBoat, Arrays.asList("C6", "C7"));
			Ship ship20 = new Ship(gp10, submarine, Arrays.asList("A2","A3", "A4"));
			Ship ship21 = new Ship(gp10, patrolBoat, Arrays.asList("G6", "H6"));

			Ship ship22 = new Ship(gp11, destroyer, Arrays.asList("B5", "C5", "D5"));
			Ship ship23 = new Ship(gp11, patrolBoat, Arrays.asList("C6", "C7"));

			Ship ship24 = new Ship(gp13, destroyer, Arrays.asList("B5", "C5", "D5"));
			Ship ship25 = new Ship(gp13, patrolBoat, Arrays.asList("C6", "C7"));
			Ship ship26 = new Ship(gp14, submarine, Arrays.asList("A2", "A3", "A4"));
			Ship ship27 = new Ship(gp14, patrolBoat, Arrays.asList("G6", "H6"));


			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);
			shipRepository.save(ship11);
			shipRepository.save(ship12);
			shipRepository.save(ship13);
			shipRepository.save(ship14);
			shipRepository.save(ship15);
			shipRepository.save(ship16);
			shipRepository.save(ship17);
			shipRepository.save(ship18);
			shipRepository.save(ship19);
			shipRepository.save(ship20);
			shipRepository.save(ship21);
			shipRepository.save(ship22);
			shipRepository.save(ship23);
			shipRepository.save(ship24);
			shipRepository.save(ship25);
			shipRepository.save(ship26);
			shipRepository.save(ship27);

			Set<String> salvoList = new HashSet<>(Arrays.asList("B5", "C5", "F1"));
			Set<String> salvoList2 = new HashSet<>(Arrays.asList("B4", "B5", "B6"));
			Set<String> salvoList3 = new HashSet<>(Arrays.asList("F2", "D5"));
			Set<String> salvoList4 = new HashSet<>(Arrays.asList("E1", "H3", "A2"));

			Salvo salvo1 = new Salvo(1, salvoList, gp1);
			Salvo salvo2 = new Salvo(2, salvoList3, gp1);
			Salvo salvo3 = new Salvo(1, salvoList2, gp2);
			Salvo salvo4 = new Salvo(2, salvoList4, gp2);

			salvoRepository.saveAll(Arrays.asList(salvo1, salvo2, salvo3, salvo4));

			// scores

			double win = 1;
			double tie = 0.5;
			double lose = 0;

			Date dateS = new Date();

			Score score1 = new Score(player1, game1, win, dateS);
			Score score2 = new Score(player2, game1, lose, dateS);

			Score score3 = new Score(player1, game2, tie, dateS);
			Score score4 = new Score(player2, game2, tie, dateS);

			Score score5 = new Score(player2, game3, win, dateS);
			Score score6 = new Score(player4, game3, lose, dateS);

			Score score7 = new Score(player2, game4, tie, dateS);
			Score score8 = new Score(player1, game4, tie, dateS);


			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
			scoreRepository.save(score7);
			scoreRepository.save(score8);

		};
	}
}


@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/web/games.html").permitAll()
				.antMatchers("/web**").permitAll()
				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/players/").permitAll()
				.antMatchers("/api/game_view/*").hasAuthority("USER")
				.antMatchers("/rest").permitAll()
				.anyRequest().permitAll();
		http.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");
		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();
		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}


}