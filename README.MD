# Gitlab CI Kotlin DSL

This is the Kotlin DSL to generate Gitlab CI yaml file

# Example

TODO

# How to download

Project is currently in development and is not available in maven central yet

# Supported Gitlab CI configuration parameters

| Block          | Supported | Gitlab Documentation link                                            |
| -------------- | --------- |--------------------------------------------------------------------- |
| default        | No        | <https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters>     | [//]: # (6)
| inherit        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#inherit>                        |
| image          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#image>                          |
| services       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#services>                       |
| script         | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#script>                         |
| before_script  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> |
| after_script   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> |
| stage          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#stage>                          |
| only           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               |
| except         | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               |
| rules          | No        | <https://docs.gitlab.com/ee/ci/yaml/#rules>                          | [//]: # (5)
| tags           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#tags>                           |
| allow_failure  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#allow_failure>                  |
| when           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#when>                           |
| environment    | No        | <https://docs.gitlab.com/ee/ci/yaml/#environment>                    | [//]: # (7)
| cache          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#cache>                          |
| artifacts      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#artifacts>                      |
| dependencies   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#dependencies>                   |
| needs          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#needs>                          |
| coverage       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#coverage>                       |
| retry          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#retry>                          |
| timeout        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#timeout>                        |
| parallel       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#parallel>                       |
| trigger        | No        | <https://docs.gitlab.com/ee/ci/yaml/#trigger>                        | [//]: # (9)
| interruptible  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#interruptible>                  |
| resource_group | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#resource_group>                 |
| include        | No        | <https://docs.gitlab.com/ee/ci/yaml/#include>                        | [//]: # (8)
| extends        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#extends>                        |
| pages          | No        | <https://docs.gitlab.com/ee/ci/yaml/#pages>                          | [//]: # (4)
| variables      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#variables>                      |

# Documentation

All supported features are documented in [Features Page](/FEATURES.MD)