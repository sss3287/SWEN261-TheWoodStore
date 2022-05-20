# E-Store:  theWoodStore

An online E-store system built in Java 8=>11 
  
## Team

- Teagan Nester
- Ainsley Ross
- Caitlyn Cyrek 
- Trevor Borden 
- Miguel Reyes

## Prerequisites

- Java 8=>11 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven


## How to run it

1. Clone the repository and go to the root directory.
2. Execute `mvn compile exec:java`
3. Open in your browser `http://localhost:8080/`
4.  use username "admin" to see site owner functionality
    otherwise, login or signup

## Known bugs and disclaimers

1. When logged in as "admin," going to a product's detail page through the search bar routes the admin to the customer version intead of the admin version of the product-detail-page. The only way to get back to the admin version of the site is to log out and log in as admin again.
2. If the admin edits or deletes a product while it is in a customer's cart, then the instance of the product in the cart is not updated or deleted.
3. The navigation bar may not display correctly on some machines (i.e. not all buttons are displayed). Replace the <nav> tag with a <div> tag in the component html files.
4. A customer can checkout when they have an empty cart. In shopping-cart.component.ts, completeOrder() should check that the size of the shopping cart is not equal to zero.

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## License

MIT License

See LICENSE for details.
