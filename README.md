[![Build Status](https://travis-ci.org/plantlogic/user-service.svg?branch=master)](https://travis-ci.org/plantlogic/user-service)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8004ecfef858423da1634b2b1e772297)](https://www.codacy.com/app/mattwebbio/user-service?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=plantlogic/user-service&amp;utm_campaign=Badge_Grade) 
[![Issues](https://img.shields.io/github/issues/plantlogic/user-service.svg?style=flat)](https://github.com/plantlogic/user-service/issues) 
[![License](https://img.shields.io/github/license/plantlogic/user-service.svg?style=flat)](https://github.com/plantlogic/user-service/blob/master/LICENSE) 
[![Docker Pulls](https://img.shields.io/docker/pulls/plantlogic/user-service.svg?style=flat)](https://hub.docker.com/r/plantlogic/user-service) 
[![Demo](https://img.shields.io/badge/demo-live-success.svg)](https://demo.plantlogic.org)
# 🌱 PlantLogic | User Service


## Environment Variables
* **APP_NAME:** Default is `PlantLogic`.
### Email
When deploying just this service, these can be set by copying the 
`smtp-config.example.env` to `smtp-config.env` and adjusting the variables
appropriately.
* **SMTP_HOST**
* **SMTP_PORT:** Default is `587`.
* **SMTP_USERNAME**
* **SMTP_PASSWORD**
* **SMTP_FROM**
* **SMTP_TLS:** `true`/`false`
(Sets whether StartTLS is required for connection. Default is `true`.)