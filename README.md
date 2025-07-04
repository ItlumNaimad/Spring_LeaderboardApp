
# Game Leaderboard System

## 📝 Opis projektu
System do zarządzania rankingami, turniejami i meczami dla różnych gier. Aplikacja umożliwia śledzenie wyników zarówno indywidualnych graczy jak i drużyn, organizację turniejów oraz prowadzenie szczegółowych statystyk.

## 🛠️ Technologie
- **Backend:**
  - Java 17
  - Spring Boot 3.5.3
  - Spring MVC
  - Spring Data JPA
  - Lombok
  - Jakarta EE
- **Frontend:**
  - Thymeleaf
  - TailwindCSS 3.4.17
  - PostCSS 8.5.6
  - Autoprefixer 10.4.21
- **Baza danych:**
  - JPA/Hibernate
  - JDBC

## 🚀 Funkcjonalności
- Zarządzanie graczami i drużynami
- Rejestracja wyników meczy (tryb indywidualny i drużynowy)
- System rankingowy
- Organizacja turniejów
- Statystyki graczy i drużyn
- Tabele wyników


## 🚦 Status projektu i znane problemy
- ✅ Podstawowy system rankingowy
- ✅ Zarządzanie graczami i drużynami
- ✅ Rejestracja wyników meczy
- ❌ Niekompletny system turniejowy:
  - Brak pełnej implementacji widoku turnieju
  - Niezaimplementowane tworzenie drabinek turniejowych
  - Brak systemu eliminacji
- ❌ Brak systemu autoryzacji i uwierzytelniania
- ❌ Brak walidacji formularzy po stronie klienta

## 🔧 Instalacja i uruchomienie

### Wymagania
- Java 17 lub nowsza
- Maven
- Node.js i npm (dla zarządzania zależnościami frontend)

### Kroki instalacji
1. Sklonuj repozytorium:
```
bash git clone [url-repozytorium]
``` 

2. Zainstaluj zależności npm:
```
bash npm install
``` 

3. Zbuduj projekt:
```
bash mvn clean install
``` 

4. Uruchom aplikację:
```
bash mvn spring-boot:run
``` 

Aplikacja będzie dostępna pod adresem `http://localhost:8080`

## 💡 Planowane rozbudowy
1. Implementacja pełnego systemu turniejowego
2. Dodanie systemu autoryzacji
3. Implementacja zaawansowanych statystyk
4. Dodanie systemu osiągnięć
5. Implementacja API REST
6. Dodanie powiadomień email

## 📝 Licencja

Ten projekt jest licencjonowany na podstawie Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.

Oznacza to, że możesz:
- Dzielić się — kopiować i rozpowszechniać utwór w dowolnym medium i formacie
- Adaptować — remiksować, przekształcać i tworzyć na podstawie utworu

Pod następującymi warunkami:
- Attribution (Uznanie autorstwa) — Musisz odpowiednio oznaczyć autorstwo
- NonCommercial (Użycie niekomercyjne) — Nie możesz używać utworu do celów komercyjnych
- ShareAlike (Na tych samych warunkach) — Jeśli zmieniasz, przekształcasz lub tworzysz na podstawie tego utworu, możesz rozpowszechniać swój utwór tylko na tej samej licencji

Pełny tekst licencji: https://creativecommons.org/licenses/by-nc-sa/4.0/
