# CHANGE LOG

## [unreleased]

### Added
- New endpoint for delete user client.

### Changed
- Change endpoint for get clients. Now can accept filters by name and id.

### Fixed

### Removed

## [1.3] - 2023-12-09

### Added
- New endpoint for put update user service.

### Changed
- Change endpoint for get services. Now can accept filters by name and id.

### Fixed
- Fix repository for delete user service.

### Removed

## [1.2.2] - 2023-12-05

### Added
- New endpoint for bulk delete services.

### Changed

### Fixed

### Removed

## [1.2.1] - 2023-12-05

### Added
- Add initial setup for set the password to admin account.
- Add initial setup for set the email to admin account.
- Add endpoint for self update user password.
- Add endpoint for validate existent user account.

### Changed
- Now the services are not shared between users.

### Fixed

### Removed
- Remove User Service entity.

## [1.1.1] - 2023-11-23

### Added
- Added validation code in the url frontend at pre-register user.
- Added cors for angular frontend on render.com.

### Changed

### Fixed

### Removed

## [1.1.0] - 2023-11-22

### Added
- Pagination for request user services.
- New endpoint for bulk delete and bulk save user services.
- Send mail confirmation for enabling user account.
- New endpoint for validate the user mail.

### Changed
- All endpoints are for logged user.
- JWT now is transferred in header requests.

### Fixed

### Removed