# API RESTful VideoGen
URL server:
```
[server] = http://localhost:8080
```
### Resources
The resources are in:
```
/video/*.mp4
/video/output/*.mp4
/img/*.png
/gif/*.gif
```
### Video list
Get a list of videos in VideoGen file:
```
GET /
or
GET: /api/v1/VideoGenList
```
Example response:
```json
[
    {
        "duration": 0,
        "image": "img/v1.png",
        "git": "gif/v1.gif",
        "probability": 0,
        "description": "This is a description of the first mandatory video",
        "id": "v1",
        "video": "src/main/resources/public/video/snus-nu.mp4",
        "type": "mandatory"
    },
    {
        "duration": 0,
        "image": "img/v2.png",
        "git": "gif/v2.gif",
        "probability": 65,
        "description": "This is a description of the optional video",
        "id": "v2",
        "video": "src/main/resources/public/video/theSimpsons1.mp4",
        "type": "optional"
    },
    {
        "0": {
            "duration": 0,
            "image": "img/v31.png",
            "git": "gif/v31.gif",
            "probability": 40,
            "description": "This is a description of the first alternative video",
            "id": "v31",
            "video": "src/main/resources/public/video/theSimpsons2.mp4",
            "type": "alternative"
        },
        "1": {
            "duration": 0,
            "image": "img/v32.png",
            "git": "gif/v32.gif",
            "probability": 60,
            "description": "This is a description of the second alternative video",
            "id": "v32",
            "video": "src/main/resources/public/video/theSimpsons3.mp4",
            "type": "alternative"
        }
    },
    {
        "duration": 0,
        "image": "img/v4.png",
        "git": "gif/v4.gif",
        "probability": 0,
        "description": "This is a description of the last mandatory video",
        "id": "v4",
        "video": "src/main/resources/public/video/theSimpsons4.mp4",
        "type": "mandatory"
    }
]
```
### Random/Probability mode
Create a random video with probabilities :
```
POST: /api/v1/random
```
Example response:
```json
{
    "message": "successful",
    "location": "video/output/output.mp4"
}
```
### Configurator mode
Create a video in configurator mode :
```
POST: /api/v1/conf
```
* data: v1, v2, v32, v4, v54, v6

Example response:
```json
{
    "message": "successful",
    "location": "video/output/output.mp4"
}
```
### Create git of a video
Create a gif of a video :
```
POST: api/v1/createGif
```
* data: video.mp4

```json
{
    "message": "successful",
    "location": "gif/output.mp4"
}
```

