# FantasyFootball

## Beschrijving

Dit project is gebaseerd op het thema **Fantasy Football**. Hierbij kan je je eigen teams aanmaken en zelf je team samenstellen door spelers hieraan toe te voegen. 
Hierna kan je deze teams het laten uitbattelen in wedstrijden.
Het project maakt gebruik van microservices om de verschillende aspecten van het thema te beheren. De services zijn ontwikkeld met Java en Maven, en worden gedistribueerd via Docker containers.

De belangrijkste componenten van het project zijn:

- **Match Service**: Behandelt de logica en data rondom wedstrijden.
- **Player Service**: Behandelt de logica en data rondom spelers.
- **Team Service**: Behandelt de logica en data rondom teams.
- **API Gateway**: Biedt een uniforme toegangspoort tot alle microservices.
