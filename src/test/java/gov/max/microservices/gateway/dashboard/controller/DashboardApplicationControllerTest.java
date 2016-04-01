package gov.max.microservices.gateway.dashboard.controller;

public class DashboardApplicationControllerTest {

    private DashboardApplicationController controller;
                          /*
	@Before
	public void setup() {
		controller = new RegistryController(new ApplicationRegistry(new SimpleApplicationStore(),
				new HashingApplicationUrlIdGenerator()));
	}

	@Test
	public void get() {
		DashboardApplication app = new DashboardApplication("http://localhost", "FOO");
		app = controller.register(app).getBody();

		ResponseEntity<Application> response = controller.get(app.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("http://localhost", response.getBody().getUrl());
		assertEquals("FOO", response.getBody().getName());
	}

	@Test
	public void get_notFound() {
		controller.register(new DashboardApplication("http://localhost", "FOO"));

		ResponseEntity<?> response = controller.get("unknown");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void applications() {
		DashboardApplication app = new DashboardApplication("http://localhost", "FOO");
		app = controller.register(app).getBody();

		Collection<DashboardApplication> applications = controller.applications(null);
		assertEquals(1, applications.size());
		assertTrue(applications.contains(app));
	}

	@Test
	public void applicationsByName() {
		DashboardApplication app = new DashboardApplication("http://localhost:2", "FOO");
		app = controller.register(app).getBody();
		DashboardApplication app2 = new DashboardApplication("http://localhost:1", "FOO");
		app2 = controller.register(app2).getBody();
		DashboardApplication app3 = new DashboardApplication("http://localhost:3", "BAR");
		controller.register(app3).getBody();

		Collection<Application> applications = controller.applications("FOO");
		assertEquals(2, applications.size());
		assertTrue(applications.contains(app));
		assertTrue(applications.contains(app2));
		assertFalse(applications.contains(app3));
	}
	*/
}
