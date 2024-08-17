![Springboot](https://shields.io/badge/v3.2.10-6DB33F?logo=springboot&label=Spring%20Boot&style=social)
![Springcloud](https://shields.io/badge/2023.0.3-6DB33F?logo=springboot&label=Spring%20Cloud&style=social)
![Gradle](https://img.shields.io/badge/v8.9-02303A?logo=gradle&label=Gradle&style=social)
![Redis](https://img.shields.io/badge/v7.4.x-FF4438?logo=redis&label=Redis&style=social)

# cooltime-server-event
- 서버사이드이벤트 전송을 위한 서버 설정
- WebFlux(Netty) 기준으로 작성
- springboot resource server 인증 기준으로 설정cooltime-gateway-service와 동일한 방식으로 세션 체크 (게이트웨이를 통하여 로그인을 하였다면 별도의 인증 없이 사용 가능)

## 1. 프로젝트 설정
> `git` 클론 후 환경에 따라 설정

#### Spring Tool Suite (STS4)

- 프로젝트를 오른클릭 후 `Configure > Add Gradle Nature` 클릭

- 빌드가 완료되면 Boot Dashboard에 프로젝트가 보임

- Boot Dashboard의 프로젝트를 오른클릭 후 `Open Config` 클릭

- `Environment` 탭을 클릭 후 `Add` 버튼 클릭

- 필드값을 아래와 같이 입력

**Name**			|**Value**
:------------------	|:--------
ENCRYPTOR_PROFILE	|<인크립트코드>

- `OK` 버튼 `Apply` 버튼 `Close` 버튼을 차례로 클릭

- Boot Dashboard의 프로젝트를 `start` 하여 서버가 정상적으로 로딩되는지 확인

#### IntelliJ

- 상단 오른쪽의 `▶` 버튼을 클릭하여 어플리케이션을 실행

- 상단 오른쪽의 `: -> Edit Configuration` 메뉴를 클릭

- `Build and run` 메뉴에 `Environment variables` 입력칸이 없다면 아래와 같이 생성
	- `Build and run` 메뉴의 `Moidfy options` 클릭

	- `Environment variables` 클릭

- Environment variables 필드값 아래와 같이 입력

**Name**			|**Value**
:------------------	|:--------
ENCRYPTOR_PROFILE	|<인크립트코드>

- `OK` 버튼 `Apply` 버튼 `Close` 버튼을 차례로 클릭

- 상단 오른쪽의 `▶` 버튼을 다시 클릭하여 어플리케이션이 정상적으로 로딩되는지 확인


## 2. 프로젝트 빌드

#### 로컬 환경

프로젝트 폴더에서 아래와 같이 실행

```shell
$ ./gradlew clean build -x test
```

#### 개발 환경

프로젝트 폴더에서 아래와 같이 실행

```shell
$ sudo chmod +x gradlew # 권한이 없다면 권한 부여
$ ./gradlew clean build -PbranchName=development -PbuildNumber=1 -DENCRYPTOR_PROFILE=<인크립트코드>

> Configure project :
[ INFO ] BRANCH_NAME    : development
[ INFO ] BUILD_VERSION  : 0.0.1
[ INFO ] PROFILE        : dev

BUILD SUCCESSFUL in 43s

```

## 3. Nginx 설정
```
location /streaming {
	proxy_http_version 1.1;
	chunked_transfer_encoding on;

	proxy_set_header Host $host;
	proxy_set_header X-Real-IP $remote_addr;
	proxy_set_header Cache-Control 'no-cache';
	proxy_set_header X-Accel-Buffering 'no';
	proxy_set_header Content-Type 'text/event-stream';
	proxy_set_header Connection keep-alive;

	proxy_read_timeout 30m;	#세션타임아웃 시간과 맞춰준다

	proxy_buffering off;
	proxy_cache off;

	proxy_pass http://cloud_stream;
}
```
