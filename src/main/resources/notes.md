## Fedora 28 - SELinux

Permission denied when nginx talks to the grpc server.

`setsebool -P httpd_can_network_connect 1`

## NGINX

http://nginx.org/en/docs/http/ngx_http_grpc_module.html#grpc_pass

Used nginx with version 1.15 (mainline) https://www.nginx.com/blog/nginx-1-6-1-7-released/

### Why ?

We recommend that in general you deploy the NGINX mainline branch at all times. The main reason to use the stable branch is that you are concerned about possible impacts of new features, such as incompatibility with third-party modules or the inadvertent introduction of bugs in new features.

### But why ?

The ngx_http_grpc_module module allows passing requests to a gRPC server (1.13.10). The module requires the ngx_http_v2_module module. 

### Config


```
http {

...

    server {
       listen 9000 http2;

        location / {
            grpc_pass 127.0.0.1:8080;
        }
    }
}
```

## GRPC Health-Check

https://github.com/grpc/grpc/blob/master/doc/health-checking.md

## Consul

https://www.consul.io/docs/agent/checks.html

gRPC + Interval - These checks are intended for applications that support the standard gRPC
health checking protocol. The state of the check will be updated at the given interval by
probing the configured endpoint. By default, gRPC checks will be configured with a default
timeout of 10 seconds. It is possible to configure a custom timeout value by specifying the
timeout field in the check definition. gRPC checks will default to not using TLS, but TLS
can be enabled by setting grpc_use_tls in the check definition. If TLS is enabled, then by
default, a valid TLS certificate is expected. Certificate verification can be turned off by
setting the tls_skip_verify field to true in the check definition.