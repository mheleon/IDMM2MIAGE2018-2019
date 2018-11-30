# Auteurs

* ADDA Raoul
* HERNANDEZ LEON Maykol

----
# TP6 - SIR [M1 Miage 2017/2018]

L’objectif de ce TP est de construire une application web basée sur une base de données pour faire la mise en place d’un MVW côté client.

En développement ce TP, nous nous sommes initié au Framework AngularJS développé par Google, qui est basé JavaScript.

Le but de ce TP est de créer une application Web pour consulter les information relatives aux Pokémons, comme ses noms, ses attaques, ses images, etc. 
Pour ce faire on utilise la base de données de l'API pokeapi.co.

Projet forké de https://github.com/barais/teaching-jxs-tp5

----

Dans ce TP nous avons utilisé les données mise à notre disposition. Nous avons avancé en répondant à chacune des question du TP.

 - Nous avons deux pages html, la première qui est  index.html ---- Elle représente la classe principale (Cette page HTML utilise une directive personnalisée pokedex)
																											
							la deuxième qui est  pokedex.html ---- Cette page nous permet de définir les balises nécessaires et le contenu de la page

 - Nous avons créé des controllers en utilisant la directive ng-controller							

 - Nous avons  aussi  eu à ajouter des variable en utilisant le service $scope
 
 - Nous avons aussi utilisé les directives  ng-click,  ng-model
 
### Accès à une API
	 l'accès à l'API ici nous permet d'accéder à la liste des Pokémon mis à notre disposition. Pour cela, nous avons utilisé les services  $http et
	 $resource(qui permet d'interroger facilement l'API à propos des types de Pokémons en fonction de leur identifiant).

	 
### Communication entre contrôleurs
	Avant de faire la communication nous avons d'abord travailler avec deux controllers (myCtrlInformation, myCtrlSearch) que nous allons pas la suite liés.
	Pour faire cette liaison, nous avons utilisé le service  $scope.$watch qui vérifie la variable serveData.myValue et met à jour l'affichage du pokémon

### Création d'une directive
	
	nous avons créé une directive nommée pokedex qui nous permet de définir le contenu de la page HTML.
	
	cette directive sera ensuite représentée dans index.html mais de la façon suivante :   <pokedex> </pokedex>
