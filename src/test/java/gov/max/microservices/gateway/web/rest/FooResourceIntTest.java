package gov.max.microservices.gateway.web.rest;

import gov.max.microservices.gateway.Application;
import gov.max.microservices.gateway.domain.Foo;
import gov.max.microservices.gateway.repository.FooRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FooResource REST controller.
 *
 * @see FooResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FooResourceIntTest {

    private static final String DEFAULT_AAA = "AAAAA";
    private static final String UPDATED_AAA = "BBBBB";

    @Inject
    private FooRepository fooRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFooMockMvc;

    private Foo foo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FooResource fooResource = new FooResource();
        ReflectionTestUtils.setField(fooResource, "fooRepository", fooRepository);
        this.restFooMockMvc = MockMvcBuilders.standaloneSetup(fooResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        foo = new Foo();
        foo.setAaa(DEFAULT_AAA);
    }

    @Test
    @Transactional
    public void createFoo() throws Exception {
        int databaseSizeBeforeCreate = fooRepository.findAll().size();

        // Create the Foo

        restFooMockMvc.perform(post("/api/foos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foo)))
                .andExpect(status().isCreated());

        // Validate the Foo in the database
        List<Foo> foos = fooRepository.findAll();
        assertThat(foos).hasSize(databaseSizeBeforeCreate + 1);
        Foo testFoo = foos.get(foos.size() - 1);
        assertThat(testFoo.getAaa()).isEqualTo(DEFAULT_AAA);
    }

    @Test
    @Transactional
    public void getAllFoos() throws Exception {
        // Initialize the database
        fooRepository.saveAndFlush(foo);

        // Get all the foos
        restFooMockMvc.perform(get("/api/foos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(foo.getId().intValue())))
                .andExpect(jsonPath("$.[*].aaa").value(hasItem(DEFAULT_AAA.toString())));
    }

    @Test
    @Transactional
    public void getFoo() throws Exception {
        // Initialize the database
        fooRepository.saveAndFlush(foo);

        // Get the foo
        restFooMockMvc.perform(get("/api/foos/{id}", foo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(foo.getId().intValue()))
            .andExpect(jsonPath("$.aaa").value(DEFAULT_AAA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFoo() throws Exception {
        // Get the foo
        restFooMockMvc.perform(get("/api/foos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFoo() throws Exception {
        // Initialize the database
        fooRepository.saveAndFlush(foo);

		int databaseSizeBeforeUpdate = fooRepository.findAll().size();

        // Update the foo
        foo.setAaa(UPDATED_AAA);

        restFooMockMvc.perform(put("/api/foos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foo)))
                .andExpect(status().isOk());

        // Validate the Foo in the database
        List<Foo> foos = fooRepository.findAll();
        assertThat(foos).hasSize(databaseSizeBeforeUpdate);
        Foo testFoo = foos.get(foos.size() - 1);
        assertThat(testFoo.getAaa()).isEqualTo(UPDATED_AAA);
    }

    @Test
    @Transactional
    public void deleteFoo() throws Exception {
        // Initialize the database
        fooRepository.saveAndFlush(foo);

		int databaseSizeBeforeDelete = fooRepository.findAll().size();

        // Get the foo
        restFooMockMvc.perform(delete("/api/foos/{id}", foo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Foo> foos = fooRepository.findAll();
        assertThat(foos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
