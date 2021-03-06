# Dockerfile to be used to run ATH itself.
#
# see docs/DOCKER.md for usage
#

FROM debian:sid
MAINTAINER ogondza@gmail.com

RUN apt-get -y update && \
    apt-get install -y \
        curl \
        docker.io \
        imagemagick \
        iptables \
        firefox-esr \
        maven \
        openjdk-8-jdk \
        vnc4server

# Allow injecting uid and git to match directory ownership
ARG uid=1000
ENV uid $uid
ARG gid=1000
ENV gid $gid

# So it is owned by root and has the permissions vncserver seems to require:
RUN mkdir /tmp/.X11-unix && chmod 1777 /tmp/.X11-unix/

RUN groupadd ath-user -g $gid && \
    useradd ath-user -u $uid -g $gid -m -d /home/ath-user

# TODO seems this can be picked up from the host, which is unwanted:
ENV XAUTHORITY /home/ath-user/.Xauthority

USER ath-user
RUN mkdir /home/ath-user/.vnc && (echo ath-user; echo ath-user) | vncpasswd /home/ath-user/.vnc/passwd
# Default content includes x-window-manager, which is not installed, plus other stuff we do not need (vncconfig, x-terminal-emulator, etc.):
RUN touch /home/ath-user/.vnc/xstartup && chmod a+x /home/ath-user/.vnc/xstartup

# Set SUID and SGID for docker binary so it can communicate with mapped socket its uid:gid we can not control. Alternative
# approach used for this is adding ath-user to the group of /var/run/docker.sock but that require root permission we do not
# have in ENTRYPOINT as the container is started as ath-user.
USER root
RUN chmod ug+s "$(which docker)"


