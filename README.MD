# TODO
- what this project is about
- how to use (documentation for each feature)
- tests
- hidden jobs

- to yaml conversion


# Supported GitlabCi configuration parameters

| Block          | Supported | Gitlab Documentation link                                          |
| -------------- | --------- |------------------------------------------------------------------- |
| default        | No        | https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters     |
| inherit        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#inherit                        |
| image          | Yes       | https://docs.gitlab.com/ee/ci/yaml/#image                          |
| service        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#services                       |
| before_script  | Yes       | https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script |
| after_script   | Yes       | https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script |
| stage          | Yes       | https://docs.gitlab.com/ee/ci/yaml/#stage                          |
| only           | No        | https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic               |
| except         | No        | https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic               |
| rules          | No        | https://docs.gitlab.com/ee/ci/yaml/#rules                          |
| tags           | Yes       | https://docs.gitlab.com/ee/ci/yaml/#tags                           |
| allow_failure  | Yes       | https://docs.gitlab.com/ee/ci/yaml/#allow_failure                  |
| when           | Yes       | https://docs.gitlab.com/ee/ci/yaml/#when                           |
| environment    | No        | https://docs.gitlab.com/ee/ci/yaml/#environment                    |
| cache          | No        | https://docs.gitlab.com/ee/ci/yaml/#cache                          |
| artifacts      | No        | https://docs.gitlab.com/ee/ci/yaml/#artifacts                      |
| dependencies   | No        | https://docs.gitlab.com/ee/ci/yaml/#dependencies                   |
| needs          | No        | https://docs.gitlab.com/ee/ci/yaml/#needs                          |
| coverage       | No        | https://docs.gitlab.com/ee/ci/yaml/#coverage                       |
| retry          | Yes       | https://docs.gitlab.com/ee/ci/yaml/#retry                          |
| timeout        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#timeout                        |
| parallel       | Yes       | https://docs.gitlab.com/ee/ci/yaml/#parallel                       |
| trigger        | No        | https://docs.gitlab.com/ee/ci/yaml/#trigger                        |
| interruptible  | Yes       | https://docs.gitlab.com/ee/ci/yaml/#interruptible                  |
| resource_group | Yes       | https://docs.gitlab.com/ee/ci/yaml/#resource_group                 |
| include        | No        | https://docs.gitlab.com/ee/ci/yaml/#include                        |
| extends        | Yes       | https://docs.gitlab.com/ee/ci/yaml/#extends                        |
| pages          | No        | https://docs.gitlab.com/ee/ci/yaml/#pages                          |
| variables      | Yes       | https://docs.gitlab.com/ee/ci/yaml/#variables                      |