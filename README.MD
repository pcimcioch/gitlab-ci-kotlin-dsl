# Gitlab CI Kotlin DSL

This is the Kotlin DSL to generate Gitlab CI yaml file

# Example

Not created yet

[//]: # (TODO provide example)

# How to download

Project is currently in development and is not available in maven central yet

[//]: # (TODO release to maven central)

# Supported Gitlab CI configuration parameters

| Block          | Supported | Gitlab Documentation link                                            |
| -------------- | --------- |--------------------------------------------------------------------- |
| default        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters>     |
| inherit        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#inherit>                        |
| image          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#image>                          |
| services       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#services>                       |
| script         | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#script>                         |
| before_script  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> |
| after_script   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> |
| stage          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#stage>                          |
| only           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               |
| except         | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               |
| rules          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#rules>                          |
| tags           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#tags>                           |
| allow_failure  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#allow_failure>                  |
| when           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#when>                           |
| environment    | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#environment>                    |
| cache          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#cache>                          |
| artifacts      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#artifacts>                      |
| dependencies   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#dependencies>                   |
| needs          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#needs>                          |
| coverage       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#coverage>                       |
| retry          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#retry>                          |
| timeout        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#timeout>                        |
| parallel       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#parallel>                       |
| trigger        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#trigger>                        |
| interruptible  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#interruptible>                  |
| resource_group | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#resource_group>                 |
| include        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#include>                        |
| extends        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#extends>                        |
| pages          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#pages>                          |
| variables      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#variables>                      |

# Documentation

All supported features are documented in [Features Page](/FEATURES.MD)

[//]: # (TODO how to run)
[//]: # (TODO 1. simple gradle project)
[//]: # (TODO 2. simple KScript project?)
[//]: # (TODO 3. child pipelines)