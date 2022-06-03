Version 4.0

    1. API is not secured
    2. Replace QueryDSL with Criteria API

Version 3.0

    1. API is not secured
    2. Used Spring Data JPA for database access, with QueryDSL library to write clean, readable, type-safe DB actions.
    3. Implemented strategy pattern for slot booking. And used builder pattern along with DTO pattern to separate out DTO and entity models.
    4. Enabled CORS
    5. Added lock when getting available slot during slot booking
    6. Added Transaction while assigning a slot to a vehicle and changing the status of slot(during slot booking)
    7. Added unit tests using JUnit framework

Version 2.0

    1. API is not secured
    2. Used Spring Data JPA for database access, with native query and Store proc for slot booking strategy.
    3. Implemented strategy pattern for slot booking. However, singleton and builder pattern are used sparignly.

Version 1.0

    1. API is not secured
    2. Used Spring Data JPA for database access, with native query and Store proc for slot booking strategy.
    3. No specific design pattern used. However, singleton and builder pattern are used sparignly.
