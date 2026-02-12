# Relazione Progetto: JTime Management System
**Studente:** 126294
**Corso:** Metodologie di Programmazione / Modellazione e Gestione della Conoscenza / Programmazione Avanzata
**Sessione:** Gennaio/Febbraio 2026

## 1. Descrizione delle Funzionalità Implementate
L'applicazione **JTime** è un prototipo per la gestione del tempo e delle attività, progettata per facilitare la pianificazione e il monitoraggio degli impegni.

Le funzionalità principali includono:
- **Gestione Progetti:** Inserimento e visualizzazione di progetti attivi. Possibilità di completare un progetto solo se non vi sono attività pendenti.
- **Gestione Attività (Tasks):** Associazione di task a progetti, con stima del tempo (ore) e registrazione della durata effettiva al completamento.
- **Pianificazione Giornaliera:** Interfaccia per assegnare attività a giorni specifici, con calcolo automatico dell'impegno totale (effort) previsto per la giornata.
- **Reportistica:** Generazione di riepiloghi organizzati per progetto e per intervallo temporale, mostrando lo stato delle attività e il confronto tra tempo stimato ed effettivo.

## 2. Responsabilità Individuate
Il sistema è stato progettato seguendo il principio di singola responsabilità (SRP):
- **Model Layer:** Gestione dell'integrità dei dati e della logica di business (es. validazione stati).
- **Persistence Layer:** Separazione netta tra il dominio e il database tramite JPA/Hibernate.
- **Controller/UI Layer:** Gestione dell'interazione utente e coordinamento tra vista e modello.
- **Manager (Composition):** Coordinamento delle operazioni complesse che coinvolgono più entità.

## 3. Classi ed Interfacce con Responsabilità
Seguendo il requisito di "programming for extensibility" e "interfaces first", il sistema si basa su un'ampia gerarchia di interfacce:

- **`Taggable`**: Interfaccia per entità che supportano etichette/categorie.
- **`TimeTrackable`**: Gestisce la logica di tracciamento temporale (stima vs realtà).
- **`Task` / `TaskImpl`**: Rappresenta l'unità di lavoro. Responsabilità: mantenere lo stato, la descrizione e le tempistiche.
- **`Project` / `ProjectImpl`**: Contenitore di attività. Responsabilità: gestire il ciclo di vita del progetto e la coerenza dei task contenuti.
- **`JTimeManager`**: Classe centrale che utilizza la **composizione** per implementare le interfacce `ProjectManager`, `PlanningManager` e `ReportManager`.

## 4. Organizzazione dei Dati e Persistenza
Il progetto utilizza **JPA (Jakarta Persistence API) con Hibernate** e un database **H2** (in-memory per il prototipo).
- **Entities**: Classi come `ProjectEntity` e `TaskEntity` sono mappate direttamente sul database relazionale.
- **Mappers**: L'uso di mapper bidirezionali (`ProjectMapper`, `TaskMapper`) garantisce che il modello di dominio rimanga puro (POJO) e non dipendente da annotazioni di persistenza, facilitando futuri cambi di storage.
- **PersistenceService**: Gestisce l'apertura delle sessioni Hibernate e garantisce la transazionalità delle operazioni.

## 5. Estendibilità e Integrazione Nuove Funzionalità
L'architettura è progettata per rispettare i principi **SOLID**:
- **Open/Closed Principle**: Grazie all'uso intensivo di interfacce e tipi generici, è possibile aggiungere nuove tipologie di task (es. `RecurringTask`) senza modificare il codice esistente.
- **Liskov Substitution**: Le implementazioni (`TaskImpl`, `ProjectImpl`) rispettano rigorosamente i contratti delle interfacce.
- **Meccanismi di estensione**: L'uso della composizione e del pattern **Factory** per la creazione degli oggetti facilita l'integrazione di nuove funzionalità (come tag gerarchici o risorse esterne) semplicemente estendendo le interfacce esistenti o aggiungendo nuovi `Manager`.

## 6. Utilizzo di Java 21 e Tecnologie Avanzate
- **Sealed Interfaces/Enums**: Gli stati (`TaskState`, `ProjectState`) utilizzano enums con comportamento polimorfico.
- **Stream API**: Utilizzate estensivamente per il filtraggio e l'aggregazione dei dati nei report.
- **Type Matching**: Uso di switch expressions per la gestione pulita degli stati degli oggetti.
