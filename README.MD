# Gitlab CI Kotlin DSL

This is the Kotlin DSL to generate Gitlab CI yaml file

# Example

TODO

# How to download

Project is currently in development and is not available in maven central yet

# Supported Gitlab CI configuration parameters

| Block          | Supported | Gitlab Documentation link                                            |
| -------------- | --------- |--------------------------------------------------------------------- |
| default        | No        | <https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters>     | [//]: # (Implement, test, document)
| inherit        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#inherit>                        | [//]: # (document)
| image          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#image>                          | [//]: # (document)
| services       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#services>                       | [//]: # (document)
| before_script  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> | [//]: # (document)
| after_script   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> | [//]: # (document)
| stage          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#stage>                          | [//]: # (document)
| only           | No        | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               | [//]: # (Implement, test, document)
| except         | No        | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               | [//]: # (Implement, test, document)
| rules          | No        | <https://docs.gitlab.com/ee/ci/yaml/#rules>                          | [//]: # (Implement, test, document)
| tags           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#tags>                           | [//]: # (document)
| allow_failure  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#allow_failure>                  | [//]: # (document)
| when           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#when>                           | [//]: # (document)
| environment    | No        | <https://docs.gitlab.com/ee/ci/yaml/#environment>                    | [//]: # (Implement, test, document)
| cache          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#cache>                          | [//]: # (document)
| artifacts      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#artifacts>                      | [//]: # (document)
| dependencies   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#dependencies>                   | [//]: # (document)
| needs          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#needs>                          | [//]: # (document)
| coverage       | No        | <https://docs.gitlab.com/ee/ci/yaml/#coverage>                       | [//]: # (Implement, test, document)
| retry          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#retry>                          | [//]: # (document)
| timeout        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#timeout>                        | [//]: # (document)
| parallel       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#parallel>                       | [//]: # (document)
| trigger        | No        | <https://docs.gitlab.com/ee/ci/yaml/#trigger>                        | [//]: # (Implement, test, document)
| interruptible  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#interruptible>                  | [//]: # (document)
| resource_group | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#resource_group>                 | [//]: # (document)
| include        | No        | <https://docs.gitlab.com/ee/ci/yaml/#include>                        | [//]: # (Implement, test, document)
| extends        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#extends>                        | [//]: # (document)
| pages          | No        | <https://docs.gitlab.com/ee/ci/yaml/#pages>                          | [//]: # (Implement, test, document)
| variables      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#variables>                      | [//]: # (document)

# Documentation

All supported features are documented in [Features Page](/FEATURES.MD)