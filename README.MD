AdServer project:
	- umieszczanie reklam
	- edycja umieszczonych reklam
	- rejestracja użytkownika
	- edycja danych użytkownika
	- kategorie reklam
	- kategoria zawartości reklamy (html/obrazek)
	- kategorie w formie drzewiastej (używanie węzła zamiast liścia określa wszystkie dowolne poniższe kategorie reklamy)
Umieszczanie reklam
-------------------
Umieszczanie reklam za pomocą formularza rejestracji reklamy - jako klient.
Klient pisze reklamę w kodzie HTML, którą chce, by była umieszczona na serwerze po stronie współpracownika (osoby, która podejmuje sie wyświetlania reklam na ekranie swojej strony internetowej).

Co potrzebne do umieszczenia reklamy?
	- wybór kategorii reklamy (z listy?)
	- treść reklamy
	- adres strony przekierowania po kliknięciu w reklame.

Czym jest reklama?
------------------
Reklama jak na dźwignię handlu przystało jest to kawałek informacyjny zachęcający do kliknięcia w nią i wejścia na inną stronę internetową. Reklama ma być generowana w sposób nieinwazyjny w kod strony oprócz ustalenia podstawowych elementów typu np <div class=reklama category=buildings /> i tyle ma interesować współpracownika. W tym momencie reklama posiada wywołanie javascriptpowe które ładuje zawartość tego diva z zewnętrzengo serwera reklam do tkórego uzyskuje dostęp. Czyli de facto reklama ma być skonstruowana na podstawie:

<script href="adserver.com/ad/script.js"  />
<div class="ad" onload="load_ad(username, category, this)" />

Gdzie kod load_ad będzie wykonywał coś na wzór:
this.innerHtml = loadHtml("https://adserver.com/ad/category/"+category);
Zarazem przekazując kod identyfikacyjny użytkownika (w pewnym nagłówku niestandardowym protokołu HTTP), lub też w querystring protokołu get w celu analizy następującej. Ładowanie podanego html prowadzi do otwarcia innego linku - do przekierownia na inną stronę reklamy za pomocą prostego zapytania redirect wqchodzącego w skład nagłówków http. W tym przypadku przekazywane są również uprawnienia użytkownika.
Przykład linku przekierowującego na strone
http://adserver.com/ad/redirect/437657?user=473856 gdzie 1. numer to numer identyfikacyjny załadowanej reklamy a user w querystring to numer identyfikacyjny użytkownika. W tym momencie w wersji podstawowej nie logujemy zdarzenia klieknięcia w reklamę  - ale zostawiamy do tego ewidentną lukę, by skorzystać z tego w odpowiedniej okazji.



Dlaczego aktualny kod śmierdzi? 
-------------------------------
Jest przestarzały (nieaktualny z założeniami) i nieprzemyślany do końca - nie działa wszystko tak jak powinno. Dobrze byłoby wyprodukować nowe api które pozwoli nam pracować o wiele wydajniej z ekspresją logiki w aplikacji - pominąć w JSP całkowicie SQL tier i zacząć pracować wyłącznie z moedelem obiektowym do tego celu należy przebudować cała istniejącą aplikacjie i kod do niej - co nie jest trudne na tym poziomie, aczkolwiek wymaga zaangazowąania i zmian wymogowych. Dlatego własnie pisany jest ten dokument, aby dogłębnie opisać potrzebne zmiany w projekcie.

Baza danych
-----------
Baza danych przedstawiać ma następujące reklacje
	- klient posiada reklamy
	- współpracownik posiada linkowanie
	- administrator posiada dosŧęp do użytkowników i reklam
	- reklama generowana jest losowo (za pomocą tier SQL lub mechanizmu w opisie struktury)

Analiza obiektowa
-----------------
Występują pewne rodzaje obiektów. Są nimi:
	- użytkownicy
	- reklamy


Co wyróżnia użytkowników?
Użytkownicy między sobą różnią się poziomem dostępu (uprawnień), nazwą własną i elementami kontaktowymi, a także poświadczeniami (których używają do uwierzytelnienia się w systemie).

Co wyróżnia reklamy?
Każda reklama zawiera inną treść dla odbiorcy. Reklamy można grupować w kategorie. Losujemy jedną reklamę z kategorii. Każda reklama ma swój numer identyfikacyjny(niekoniecznie pochodzący z bazy danych). Każda reklama posiada swojego twórce (klienta).

Jakie występują relacje w projekcie?
------------------------------------
	Reklama:
		- nr identyfikacyjny (jawny)
		- twórca (identyfikacyjny)
		- rodzaj (html/picture)
		- kategoria (branża)

	Użytkownik:
		- email (login)
		- numer identyfikacyjny
		- rodzaj uprawnien (CLI, PAR, ADM)
		- hasło w formie hashowanej MD5 (do weryfikacji)
		- dane kontaktowe:
			- telefon
			- imię i nazwisko

Widoki dostępne w aplikacji:
----------------------------

Dostępne dla użytkownika anonimowego:
	- logowanie - podanie hasła i username - już dostępne
	- rejestracja użytkownika - podanie danych kontaktowych i wybór rodzaju rejestracji - już dostępne
Dostępne dla administratora:
	- zarządzanie reklamami (baza danych) - lista, dodawanie usuwanie i edycja
	- zarządzanie użytkownikami - lista, dodawanie usuwanie i edycja - już dostępne
Dostępne dla klienta:
	- zarządzanie własnymi reklamami (lista reklam) - wspólna dla administratora (z tym, że administrator jest w stanie pobrać wszystkie możliwe)
	- dodawanie reklamy
Dostępne dla partnera:
	- generator kodu reklamy


Funkcjonalności potrzebne dla modelu:
-------------------------------------

Reklama:
	- dodawanie reklamy
	- usuwanie reklamy
	- update reklamy
	- lista wszystkich reklam
	- lista reklam użytkownika
	- lista reklam kategorii x (i potomnych kategorii)
Użytkownik:
	- dodawanie użytkownika
	- usuwanie użytkownika (i wszystkich jego posiadanych przedmiotów)
	- update danych użytkownika
