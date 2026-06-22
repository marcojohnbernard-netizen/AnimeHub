# AnimeHub

Spring Boot web app para sa 2nd year Java project: hanapin, i-rank, at subaybayan ang mga anime. Gumagamit ng **Jikan API** (legal, libre, walang authentication kailangan — pinapagana ng MyAnimeList data) para sa lahat ng anime information, poster, at official trailer. **Walang video streaming/hosting ng actual episodes** dito — tingnan ang paliwanag sa baba.

## ⚠️ Mahalagang paalala bago ka magpatuloy

Ang orihinal mong request ay isang site na "pwede makanood ng latest anime" na may "servers" at subtitles — ito ang structure ng mga piracy/illegal streaming site (gogoanime-type), kung saan ang totoong copyrighted episodes ang ihohost o iliink. Hindi ko ito ginawa.

Sa halip, ginawa kong **anime info + tracking hub** (parang MyAnimeList/AniList companion app) gamit:
- Legal na metadata API (Jikan) para sa title, synopsis, score, genres, poster
- **Official YouTube trailers/PVs lang** (legal, naka-embed mula sa YouTube) — hindi buong episodes
- Sariling watchlist system (database mo, hindi pirated content)

Ipinapakita pa rin nito lahat ng skills na hinihingi ng project mo: Spring Boot, database (JPA), security/login, external API integration, search, ranking, caching, error handling, at responsive design — pero walang legal risk.

## Mga Features

- 🆕 **Latest Anime** - kasalukuyang umaaireng season (homepage)
- 🏆 **Top Ranking** - leaderboard base sa MyAnimeList score, may pagination
- 🔍 **Search** - maghanap ng kahit anong anime
- 📄 **Anime Detail** - synopsis, genres, score, episode count, official trailer
- 👤 **Login/Register** - secure na account system (BCrypt password hashing)
- 📋 **My List (Watchlist)** - i-add/alisin ang anime, baguhin ang status (Plan to Watch / Watching / Completed / Dropped)
- ⚡ **Caching** - 10-minute cache sa mga API calls para mabilis at hindi mahit ang rate limit ng Jikan
- 🛡️ **Error handling** - friendly error messages kapag bumagsak/nag-timeout ang external API
- 📱 **Responsive** - gumagana sa mobile at desktop

## Paano patakbuhin

**Requirements:** Java 17+, Maven (o gamitin ang IntelliJ/Eclipse na may built-in Maven)

```bash
cd animehub
mvn spring-boot:run
```

Tapos buksan ang **http://localhost:8080**

Walang kailangang i-setup na external database — gumagamit ng H2 in-memory DB (nareresेt ang data tuwing irerestart ang app). Para tingnan ang DB habang tumatakbo: **http://localhost:8080/h2-console** (JDBC URL: `jdbc:h2:mem:animehub`, username: `sa`, walang password).

### Paglipat sa MySQL (optional, kung kailangan mo permanenteng data)

1. I-uncomment ang `mysql-connector-j` dependency sa `pom.xml`
2. Palitan ang `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/animehub?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## Project Structure

```
src/main/java/com/animehub/
├── config/        - Security, RestTemplate, Cache configuration
├── controller/     - HTTP routes (Home, Anime, Auth, Watchlist)
├── service/        - Business logic (Jikan API calls, User, Watchlist)
├── repository/     - Database access (Spring Data JPA)
├── model/          - Database entities (User, WatchlistItem)
├── dto/            - Mga shape ng Jikan API responses
└── exception/      - Custom error handling

src/main/resources/
├── templates/      - Thymeleaf HTML pages
├── static/css/     - Stylesheet (design tokens nasa simula ng style.css)
└── static/js/      - Maliit na JS enhancements
```

## Mga puwedeng idagdag pa (kung gusto mong palawakin)

- User ratings/reviews per anime (bagong entity + relasyon sa User)
- Genre-based filtering sa search
- "Currently watching" progress tracker (episode number na naabot mo)
- Admin dashboard para tingnan ang lahat ng registered users
- Unit tests gamit ang `spring-boot-starter-test` (nakasama na sa pom.xml)

## Mga gamit na teknolohiya

Spring Boot 3.3, Spring Security 6, Spring Data JPA, Thymeleaf, H2 Database, Caffeine Cache, Jikan API v4
