Version 8.0

    1. Added JWT filter(interceptor) which parses the JWT token present in the header
    2. Added custom AccessDecisionManager with only WebExpressionVoter as the Voter.
    3. Added multi datasource configuration
        a. parking lot DB
        b. auth service DB
    4. Added Transactional for BookedSlotService
    5. Updated Transactional for SlotBookingService
    6. Added: HBR SQL query logging, batch processing, JPA TXN MGMT logging, HBR basic binder, ordering inserts 
    7. Added pagination support for slot retrieval 
    8. Moved ID generation from IDENTITY to SEQUENCE
    9. Added bulk insert for floors and slots
    10. Bulk delete of slots 

Version 7.0

    1. Banner added
    2. Adding Spring Security:
        a. using Fake DB authentication - In memory user account
        b. using basic auth
        c. using custom UserDetailsService
        d. disabled CSRF
    3. Migrate to Postgresql
    4. Version updated in Pom.xml
    5. Added a maven property for spring-boot version.

Version 6.0

    1. Add JPA Specifications

Version 5.0

    1. Add JPA meta model classes for type-safety

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
