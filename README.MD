# TODO
- what this project is about
- how to use (documentation for each feature)
- tests
- javaDocs?
- to yaml conversion
- when doing dependencies don't forget about: https://docs.gitlab.com/ee/ci/yaml/#artifactspaths

tomorrow:
- dependencies
- needs
- yaml conversion
- tests for yaml conversion and validation


# Supported GitlabCi configuration parameters

| Block          | Supported | Gitlab Documentation link                                          |
| -------------- | --------- |------------------------------------------------------------------- |
| default        | No        | https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters     | [//]: # (Implement, test, document)
| inherit        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#inherit                        | [//]: # (test, document)
| image          | Yes       | https://docs.gitlab.com/ee/ci/yaml/#image                          | [//]: # (test, document)
| service        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#services                       | [//]: # (test, document)
| before_script  | Yes       | https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script | [//]: # (test, document)
| after_script   | Yes       | https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script | [//]: # (test, document)
| stage          | Yes       | https://docs.gitlab.com/ee/ci/yaml/#stage                          | [//]: # (test, document)
| only           | No        | https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic               | [//]: # (Implement, test, document)
| except         | No        | https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic               | [//]: # (Implement, test, document)
| rules          | No        | https://docs.gitlab.com/ee/ci/yaml/#rules                          | [//]: # (Implement, test, document)
| tags           | Yes       | https://docs.gitlab.com/ee/ci/yaml/#tags                           | [//]: # (test, document)
| allow_failure  | Yes       | https://docs.gitlab.com/ee/ci/yaml/#allow_failure                  | [//]: # (test, document)
| when           | Yes       | https://docs.gitlab.com/ee/ci/yaml/#when                           | [//]: # (test, document)
| environment    | No        | https://docs.gitlab.com/ee/ci/yaml/#environment                    | [//]: # (Implement, test, document)
| cache          | Yes       | https://docs.gitlab.com/ee/ci/yaml/#cache                          | [//]: # (test, document)
| artifacts      | Yes       | https://docs.gitlab.com/ee/ci/yaml/#artifacts                      | [//]: # (test, document)
| dependencies   | No        | https://docs.gitlab.com/ee/ci/yaml/#dependencies                   | [//]: # (Implement, test, document)
| needs          | No        | https://docs.gitlab.com/ee/ci/yaml/#needs                          | [//]: # (Implement, test, document)
| coverage       | No        | https://docs.gitlab.com/ee/ci/yaml/#coverage                       | [//]: # (Implement, test, document)
| retry          | Yes       | https://docs.gitlab.com/ee/ci/yaml/#retry                          | [//]: # (test, document)
| timeout        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#timeout                        | [//]: # (test, document)
| parallel       | Yes       | https://docs.gitlab.com/ee/ci/yaml/#parallel                       | [//]: # (test, document)
| trigger        | No        | https://docs.gitlab.com/ee/ci/yaml/#trigger                        | [//]: # (Implement, test, document)
| interruptible  | Yes       | https://docs.gitlab.com/ee/ci/yaml/#interruptible                  | [//]: # (test, document)
| resource_group | Yes       | https://docs.gitlab.com/ee/ci/yaml/#resource_group                 | [//]: # (test, document)
| include        | No        | https://docs.gitlab.com/ee/ci/yaml/#include                        | [//]: # (Implement, test, document)
| extends        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#extends                        | [//]: # (test, document)
| pages          | No        | https://docs.gitlab.com/ee/ci/yaml/#pages                          | [//]: # (Implement, test, document)
| variables      | Yes       | https://docs.gitlab.com/ee/ci/yaml/#variables                      | [//]: # (test, document)