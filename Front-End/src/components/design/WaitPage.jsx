import React from "react";
import { motion } from "framer-motion";

const ballStyle = {
  display: "block",
  width: "5rem",
  height: "5rem",
  backgroundColor: "black",
  borderRadius: "2.5rem"
};

const bounceTransition = {
  y: {
    duration: 0.8,
    repeat: Infinity,
    ease: "easeOut"
  },
  backgroundColor: {
    duration: 0,
    repeat: Infinity,
    ease: "easeOut",
    repeatDelay: 1.6
  }
};

export default function BouncingBall() {
  return (
    <div
      style={{
        width: "5rem",
        height: "5rem",
        display: "flex",
        justifyContent: "space-around"
      }}
    >
      <motion.span
        style={ballStyle}
        transition={bounceTransition}
        animate={{
          y: ["100%", "-100%"],
          backgroundColor: ["rgb(1, 131, 1)", "rgb(1, 131, 1)"]
        }}
      />
    </div>
  );
}