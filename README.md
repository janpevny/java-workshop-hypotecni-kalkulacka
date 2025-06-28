# Kalkulačka splátkového kalendáře

Tato jednoduchá desktopová aplikace v Javě slouží k výpočtu splátkového kalendáře úvěru a jeho exportu do CSV souboru. Aplikace je vybavena grafickým uživatelským rozhraním (GUI) vytvořeným pomocí Swing a vizualizací rozdělení splátek pomocí koláčového grafu (JFreeChart).

## Funkce

*   Výpočet měsíčních splátek úvěru (anuitní metoda).
*   Generování detailního splátkového kalendáře.
*   Zobrazení souhrnných informací o úvěru (pravidelná splátka, celkem zaplaceno, celkové úroky).
*   Vizualizace rozdělení celkových splátek na jistinu a úroky pomocí koláčového grafu.
*   Export splátkového kalendáře do CSV souboru.

## Předpoklady

Pro spuštění a kompilaci této aplikace potřebujete:

### 1. Java Development Kit (JDK)

*   **Verze**: 11 nebo vyšší.
*   **Instalace na macOS (doporučeno Homebrew)**:
    1.  Otevřete terminál.
    2.  Pokud nemáte Homebrew, nainstalujte jej:
        ```bash
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        ```
    3.  Nainstalujte JDK (např. OpenJDK 17):
        ```bash
        brew install openjdk@17
        ```
    4.  Postupujte podle pokynů Homebrew pro nastavení `PATH` (obvykle se zobrazí po instalaci).

*   **Instalace na Windows**:
    1.  Stáhněte si instalační program JDK (např. OpenJDK nebo Oracle JDK) z oficiálních stránek (např. [Adoptium](https://adoptium.net/temurin/releases/) nebo [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)).
    2.  Spusťte instalační program a postupujte podle pokynů.
    3.  **Nastavte proměnnou prostředí `JAVA_HOME`** na cestu k nainstalovanému JDK (např. `C:\Program Files\Java\jdk-17`).
    4.  **Přidejte `%JAVA_HOME%\bin` do proměnné prostředí `Path`**.
        *   Vyhledejte "Proměnné prostředí" v nabídce Start.
        *   Klikněte na "Upravit proměnné prostředí systému".
        *   V okně "Vlastnosti systému" klikněte na "Proměnné prostředí...".
        *   V sekci "Systémové proměnné" najděte `Path`, vyberte ji a klikněte na "Upravit...".
        *   Přidejte novou položku `%JAVA_HOME%\bin`.

### 2. Apache Maven

*   **Verze**: 3.x nebo vyšší.
*   **Instalace na macOS (doporučeno Homebrew)**:
    1.  Otevřete terminál.
    2.  Nainstalujte Maven:
        ```bash
        brew install maven
        ```

*   **Instalace na Windows**:
    1.  Stáhněte si binární ZIP archiv Mavenu z [oficiálních stránek](https://maven.apache.org/download.cgi).
    2.  Rozbalte archiv do vhodného adresáře (např. `C:\Program Files\Apache\maven`).
    3.  **Nastavte proměnnou prostředí `M2_HOME`** na cestu k rozbalenému adresáři Mavenu (např. `C:\Program Files\Apache\maven`).
    4.  **Přidejte `%M2_HOME%\bin` do proměnné prostředí `Path`** (stejným způsobem jako u JDK).

### Závislosti projektu

Tento projekt používá následující externí knihovny, které jsou automaticky spravovány Mavenem:

*   **JFreeChart**: Knihovna pro tvorbu grafů, použitá pro vizualizaci rozdělení splátek.
    *   `groupId: org.jfree`
    *   `artifactId: jfreechart`
    *   `version: 1.0.19`

## Kompilace a sestavení

Pro zkompilování projektu a vytvoření spustitelného JAR souboru (tzv. "fat JAR", který obsahuje všechny závislosti) postupujte následovně:

1.  Otevřete terminál nebo příkazový řádek.
2.  Přejděte do kořenového adresáře projektu `loan_project`:
    ```bash
    cd /Users/yasinsezgin/loan_project
    ```
3.  Spusťte Maven příkaz pro vyčištění, kompilaci a sestavení JAR souboru:
    ```bash
    mvn clean compile assembly:single
    ```
    Po úspěšném dokončení tohoto příkazu naleznete spustitelný JAR soubor v adresáři `target` pod názvem `loan-calculator-1.0-SNAPSHOT-jar-with-dependencies.jar`.

## Spuštění aplikace

Po zkompilování projektu můžete aplikaci spustit pomocí následujícího příkazu z kořenového adresáře projektu `loan_project`:

```bash
java -jar target/loan-calculator-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Otevře se grafické okno aplikace, kde můžete zadávat parametry úvěru, vypočítat kalendář, zobrazit souhrnné informace a exportovat data.

## Jak provádět změny v kódu

Pokud provedete jakékoli změny ve zdrojovém kódu (soubory `.java`), je nutné projekt znovu zkompilovat, aby se změny projevily v aplikaci. Postupujte podle kroků v sekci "Kompilace a sestavení" a poté aplikaci znovu spusťte.

## Přispívání

Pokud byste chtěli přispět k tomuto projektu, můžete:

1.  Forkovat repozitář.
2.  Vytvořit novou větev (`git checkout -b feature/your-feature-name`).
3.  Provést své změny a otestovat je.
4.  Commitovat své změny (`git commit -m "Popis vaší změny"`).
5.  Pushnout větev na váš fork (`git push origin feature/your-feature-name`).
6.  Vytvořit Pull Request.
