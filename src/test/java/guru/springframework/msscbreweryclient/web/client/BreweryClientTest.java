package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BreweryClientTest {

    @Autowired
    BreweryClient client;

    @Test
    void testGetBeerById(){
        BeerDto dto = client.getBeerById(UUID.randomUUID());

        assertNotNull(dto);
    }

    @Test
    void testSaveNewBeer(){
        BeerDto newBeer = BeerDto.builder().beerName("Gulder").build();
        URI uri = client.saveBeerDTO(newBeer);

        assertNotNull(uri);
        System.out.println(uri.toString());
    }

    @Test
    void testUpdateBeer(){
        BeerDto updateBeer = BeerDto.builder().beerName("Gulder").beerStyle("Chill cold").build();

        client.updateBeer(UUID.randomUUID(), updateBeer);
    }

    @Test
    void textDeleteBeer(){
        client.deleteBeer(UUID.randomUUID());
    }

    @Test
    void testGetCustomerById(){
        CustomerDto dto = client.getCustomerById(UUID.randomUUID());

        assertNotNull(dto);
    }

    @Test
    void testSaveNewCustomer(){
        CustomerDto newCustomer = CustomerDto.builder().name("Amaka").build();
        URI uri = client.saveCustomer(newCustomer);

        assertNotNull(uri);
        System.out.println(uri.toString());
    }

    @Test
    void testUpdateCustomer(){
        CustomerDto updateCustomer = CustomerDto.builder().name("Yusuf").build();

        client.updateCustomer(UUID.randomUUID(), updateCustomer);
    }

    @Test
    void textDeleteCustomer(){
        client.deleteCustomer(UUID.randomUUID());
    }
}