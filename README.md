<h1>Entity-relationship diagram for the database </h1>
<img src="https://github.com/mateo1110/SpringCommerce/assets/121823252/2abaec5e-df63-4a11-8575-58de537e9a23">
Relationship users-roles is many-to-many :  one user can be associated with multiple roles and a role can be associated with multiple userr
Relationship users-cart is one-to-many :  one user can be associated with multiple cart and a cart can be associated with a user<br>

Relationship cart-cart_item is one-to-many :  one single cart can be associated with multiple cart_item with it however each cart items is associated with only on cart<br>
Relationship users-orders is one-to-many : same relation of  users-cart
Relationship orders-ordersdetails is one-to-many : same relation cart-cartitems
Relationship product-manufacture is many-to-many : one product can be associated with manufacture and manufacture can be associated with multiple product<br>
Relationship category-product is one-to-many :  one single category can be associated with multiple product with it however each product  is associated with only on category<br>
Relationship products-image is one-to-many : one single product can be associated with multiple image with it however each image  is associated with only on product <br>
Relationship products-orderdetail and products-cartitem is one to many

<h1>API</h1>
<p>Product</p>
Product

![Screenshot 2023-08-09 001654](https://github.com/mateo1110/SpringCommerce/assets/121823252/863482df-fd98-4172-aa77-81e63cfc654f)
![Screenshot 2023-08-09 001756](https://github.com/mateo1110/SpringCommerce/assets/121823252/ad6b2d32-a1fe-492d-8de9-f87ac06e9854)
![Screenshot 2023-08-09 001830](https://github.com/mateo1110/SpringCommerce/assets/121823252/2bfcf80c-ac07-405e-94ac-58d796e80f95)
![Screenshot 2023-08-09 002017](https://github.com/mateo1110/SpringCommerce/assets/121823252/a68d8a1d-8948-465c-853d-97d3434ca56f)

Order

![Screenshot 2023-08-09 002120](https://github.com/mateo1110/SpringCommerce/assets/121823252/818dd736-f5ae-4cfa-aeb2-91c538538a4e)
![Screenshot 2023-08-09 002328](https://github.com/mateo1110/SpringCommerce/assets/121823252/b48512ea-8a0e-423f-a3d2-283b9196c616)
![Screenshot 2023-08-09 002141](https://github.com/mateo1110/SpringCommerce/assets/121823252/85b82579-2d80-4c7b-8d84-4f5ef0524927)
![Screenshot 2023-08-09 002213](https://github.com/mateo1110/SpringCommerce/assets/121823252/6deaa3bc-cb16-4c81-a952-42a5eb05a601)
![Screenshot 2023-08-09 002300](https://github.com/mateo1110/SpringCommerce/assets/121823252/a1585860-8a4f-4341-bc08-d8bb1cc12bc7)
![Screenshot 2023-08-09 002328](https://github.com/mateo1110/SpringCommerce/assets/121823252/5488348f-bfd5-4bfb-a8d4-45f39f2f9e1e)

 A brief explanation for software development principles, patterns and practices being applied.

SOLID principles: Exhibited here
S: Single Responsibility Principle is exhibited here in that a controller should handle a specific HTTP request and respond with a corresponding HTTP response.<br>
L: Liskov Substitution Principle is exhibited by the UserDetailImp objects being inherited from the UserDetail class.<br>
D: Dependency Inversion Principle is exhibited by the use of dependency injection of spring boot.<br>
MVC pattern:

Exhibited here in that the source code structure is divided into 3 parts CONTROLLER responsible for accepting requests from the user, service responsible for processing business tasks by calling and integrating the corresponding repository to manipulate the database, VIEW shown in HTML files to render and display to the user, MODEL shown in specific managed objects such as entities.
 A brief explanation for the code structure.
The code structure follows the Model-View-Controller (MVC) architectural pattern, which separates the application into three main components:
Model: This component is responsible for managing the data of the application. It includes entities, repositories, and services that interact with the database.

<pre class="notranslate">
  <code>
  | - model
  |   | - entities
  |   | - repositories
  |   | - services
  </code>
</pre>
View: This component is responsible for rendering the user interface of the application. It includes HTML, CSS, and JavaScript files that are served to the client's browser.
<pre class="notranslate">
  <code>
| - resources
|   | - static
|   |   | - js
|   |   |   | - *.js
|   |   | - css
|   |   |   | - *.css
|   | - templates
|   |   | - *.html
  </code>
</pre>
Controller: This component acts as an intermediary between the Model and View components. It receives user requests, processes them, and returns responses. It includes controllers that handle HTTP requests and map them to the appropriate service methods.
<pre class="notranslate">
  <code>
| - controller
|   | - *Controller.java
  </code>
</pre>

<ul>
  <li>B1: Set port chạy ở 3306: jdbc:mysql://localhost:3306/project</li>
  <li>Create database mysql</li>
  <li>chạy chương trình</li>
</ul>
