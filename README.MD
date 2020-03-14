# TODO
- what this project is about
- how to use (documentation for each feature)
- tests

- to yaml conversion


# Supported GitlabCi configuration parameters

| Block             | Supported | Gitlab Documentation link                                          |
| ----------------- | --------- |------------------------------------------------------------------- |
| default           | No        | TODO                                                               |
| inherit           | Yes       | https://docs.gitlab.com/ee/ci/yaml/#inherit                        |
| image             | Yes       | https://docs.gitlab.com/ee/ci/yaml/#image                          |
| service           | Yes       | https://docs.gitlab.com/ee/ci/yaml/#services                       |
| before_script     | Yes       | https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script |
| after_script      | Yes       | https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script |
| stage             | Yes       | https://docs.gitlab.com/ee/ci/yaml/#stage                          |
| only              | No        | https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic               |
| except            | No        | https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic               |
| rules             | No        | https://docs.gitlab.com/ee/ci/yaml/#rules                          |
| allow_failure     | Yes       | https://docs.gitlab.com/ee/ci/yaml/#allow_failure                  |
| when              | Yes       | https://docs.gitlab.com/ee/ci/yaml/#when                           |