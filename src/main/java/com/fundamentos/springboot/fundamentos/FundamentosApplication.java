package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	//Dependencias
	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;

	private MyBeanWithDependency myBeanWithDependency;

	private MyBeanWithProperties myBeanWithProperties;

	private UserPojo userPojo;

	private UserRepository userRepository;

	private UserService userService;

	//Con @Qualifier inyectamos la clase a implementar en el constructor
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService){
		//Llamando a la propiedad
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUsersInDatabase();
		//getInformationJpqLFromUser();
		//saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "TestTransactional1@domain.com", LocalDate.now());
		User test2 = new User("TestTransactional2", "TestTransactional2@domain.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "TestTransactional3@domain.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "TestTransactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1,test2,test3,test4);

		try{
			userService.saveTransactional(users);
		} catch (Exception e){
			LOGGER.error("Este es una excepcion dentro del metodo transactional "+e);
		}

		userService.getAllUsers().stream().forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transactional: "+user));
	}

	private void getInformationJpqLFromUser(){
		LOGGER.info("Usuario con el metodo findByUserEmail" + userRepository.findByUserEmail("juan@domain.com").orElseThrow(()-> new RuntimeException("No se encontro el usuario")));

		//En esta instrucción ordena e imprime los resultados de busqueda de usuario con la letra J de manera ascendente
		userRepository.findAndSort("J", Sort.by("id").descending()).stream().forEach(user -> LOGGER.info("usuario con metodo sort "+user));

		//Llamar a query methods
		userRepository.findByName("Juan").stream().forEach(user -> LOGGER.info("Usuario con query method"+user));
		LOGGER.info("Usuario con query method findByEmailAndName"+userRepository.findByEmailAndName("jane@domain.com","Jane").orElseThrow(()-> new RuntimeException("Usuario no encontrado")));

		//Mostrar por letra que contenga el usuario
		userRepository.findByNameLike("%a%").stream().forEach(user -> LOGGER.info("Usuario findByNameLike"+user));

		//Mostrar por nombre o email
		userRepository.findByNameOrEmail( null,"juan@domain.com").stream().forEach(user -> LOGGER.info("Usuario findByNameOrEmail"+user));

		//Mostrar por intervalo de fechas, birthdate se declaro en user por eso esta en minuscula
		userRepository.findBybirthdateBetween(LocalDate.of(2019,5,28), LocalDate.of(2020,4,25)).stream().forEach(user -> LOGGER.info("Usuario findBybirthdateBetween"+user));

		//Mostrar los resultados usando like por letra que contenga el usuario en orden descendente de id
		userRepository.findByNameLikeOrderByIdDesc("%n%").stream().forEach(user -> LOGGER.info("Usuario encontrado con like y ordenado"+user));

		//Mostrar los resultados por letra que contenga el usuario en orden descendente de id
		userRepository.findByNameContainingOrderByIdDesc("a").stream().forEach(user -> LOGGER.info("Usuario encontrado sin like y ordenado"+user));

		//Mostrar los resultados usando JPQL con named parameters
		LOGGER.info("El usuario a partir del named parameter es: "+
		userRepository.getAllByBirthdateAndEmail(LocalDate.of(2020,4,25), "jane@domain.com").orElseThrow(()-> new RuntimeException("No se encontro el usuario a partir del named parameter")));
	}

	private void saveUsersInDatabase(){
		User user1 = new User("Jhon", "jhon@domain.com", LocalDate.of(2021,3,20));
		User user2 = new User("Jane", "jane@domain.com", LocalDate.of(2020,4,25));
		User user3 = new User("Juan", "juan@domain.com", LocalDate.of(2018,8,22));
		User user4 = new User("Jina", "jina@domain.com", LocalDate.of(2019,5,28));
		List<User> List = Arrays.asList(user1,user2,user3,user4);
		List.stream().forEach(userRepository::save);
	}

	public void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail()+" - "+userPojo.getPassword());
		LOGGER.error("Esto es un error");
	}
}
