
```bash
docker pull mysql:8.0.41-debian
```

```bash

$ docker run  --name mysql_db \
              -p 3306:3306 \
              -e MYSQL_ROOT_PASSWORD=root \
              -d mysql:8.0.41-debian
```