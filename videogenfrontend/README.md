# Auteurs

* HERNANDEZ LEON Maykol
* ELMAIZI Sohaib

----
# VideoGen Front-end

L’objectif de ce projet Front-end est de construire une application web basée sur le Back-end VideoGen.

Le but de ce TP est de créer une application Web pour generer des vidéos à partir de sequences de vidéo. Les vidéos generées peuvent être telechargées en format gif et aussi il est possible de consulter sa taille et sa durée. Pour ce faire on utilise le Back-end VideoGen et le projet VideoGenToolSuite des TPs du cours d'IDM 2018/2019.

----

## Getting Started

Ces instructions permettront d'obtenir une copie du projet opérationnel en locale à des fins de développement et de test.

### Tech

Ce projet utilise le suivants outils :

* [AngularJS](https://angularjs.org)
* [HTML5](https://www.w3.org/TR/html5/)
* [Git](https://git-scm.com/)

### Prérequis
Nous avons besoin d'avoir installé Git et un navigateur qui supporte JavaScript et HTML5.


### Installation

1. Cloner le projet.

```sh
$ git clone https://gitlab.com/hmaykol/SIR
$ cd videogenfront-end
```

2. Ouvir le fichier index.html avec le navigateur web. Le Back-end a été développé de tel façon de permettre avoir le ce Front-end dans le même serveur. Cependant, il est possible que lorsque le navigateur tente d'obtenir la ressource, il pense qu'il provient d'un domaine distinct, ce qui n'est pas autorisé. Bien que certains navigateurs le permettent.
Pour le réssoudre, vous pouvez utiliser Firefox, un serveur (e.i. Apache) ou bien lancer un serveur web Python :
```sh
cd videogenfrontend
python -m SimpleHTTPServer
```
Puis, aller sur localhost:8000 dans votre navigateur et la page web sera affiché.

----
# License

MIT
