package com.yoogurt.taxi.licences.common.shiro;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authz.Permission;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Getter
@Setter
public class PathPermission implements Permission {

    private String pathString;

    private PathMatcher pathMatcher;

    public PathPermission(String pathString) {
        this(pathString, new AntPathMatcher());
    }

    public PathPermission(String pathString, PathMatcher pathMatcher) {
        this.pathString = pathString;
        this.pathMatcher = pathMatcher;
    }

    @Override
    public boolean implies(Permission p) {
        return p instanceof PathPermission && pathMatcher.match(this.pathString, ((PathPermission) p).getPathString());
    }

    @Override
    public String toString() {
        return "path => " + pathString;
    }
}
