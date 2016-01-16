package org.simplifide.utils

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andy on 12/30/2015.
  */

import scala.concurrent.ExecutionContext.Implicits.global


object FutureUtils {

  /** Convert L[T] with f(T)=>F[Option[O]] => F[L[T]] */
  def flattenListOption[T,O](input:List[T])(f:(T)=>Future[Option[O]]):Future[List[O]] = {
    val res = input.map(x => f(x))
    val seq = Future.sequence(res)
    val ret = seq.map(y => {
      y.flatten
    })
    ret
  }

  /** Ziop Operation on 3 Futures */
  def alwaysZip3[A,B,C,O](x:Future[A], y:Future[B], z:Future[C])(f:(A,B,C)=>O):Future[O] = {
    val comb = ((x zip y) zip z)
    val res  = comb.map(x => {
      f(x._1._1,x._1._2,x._2)
    })
    res
  }

  def flattenFutureListOption[T,O](input:Future[List[T]], f:(T)=>Future[Option[O]]):Future[List[O]] = {
    val res = input.map(x => flattenListOption[T,O](x)(f))
    res.flatMap(identity)
  }


  def alwaysMap[T,O](input:Future[Iterator[T]])(f:(T)=>O):Future[Iterator[O]] = {
    input.map(x => {
      x.map(y => f(y))
    })
  }

  def alwaysMap[T,O](input:Iterator[Future[T]])(f:(T)=>O):Future[Iterator[O]] = {
    val seq = Future.sequence(input)
    alwaysMap(seq)(f)
  }


  def always[O,A, M[X] <: TraversableOnce[X]](input:Future[M[A]])(f:(M[A])=>M[O])(implicit cbf: CanBuildFrom[M[Future[A]], A, M[A]], executor: ExecutionContext): Future[M[O]] = {
    input.map(x => f(x))
  }

  def always[O,A, M[X] <: TraversableOnce[X]](input:M[Future[A]])(f:(M[A])=>M[O])(implicit cbf: CanBuildFrom[M[Future[A]], A, M[A]], executor: ExecutionContext): Future[M[O]] = {
    val res = Future.sequence(input)
    res.map(x => f(x))
  }

  /*
  def alwaysFlat[O,A, M[X] <: TraversableOnce[X]](input:Future[M[A]])(f:(M[A])=>O)(implicit cbf: CanBuildFrom[M[Future[A]], A, M[A]], executor: ExecutionContext): Future[O] = {
    input.map(x => f(x))
  }
  */


  def alwaysFlat[O,A, M[X] <: TraversableOnce[X]](input:M[Future[A]])(f:(M[A])=>O)(implicit cbf: CanBuildFrom[M[Future[A]], A, M[A]], executor: ExecutionContext): Future[O] = {
    val res = Future.sequence(input)
    res.map(x => f(x))
  }


  /** Map over a list of elements with a function that converts to a future and flattens */
  def mapFuture[O,A](input:List[A])(f:(A)=>Future[O]):Future[List[O]] = {
    val result = input.map(x => f(x))
    Future.sequence(result)
  }



  // Handles a future of a future flattening
  def alwaysItemFlatten[O,A](input:Future[A])(f:(A)=>Future[O]) = {
    val result = input.map(x => f(x)).flatMap(identity)
    result
  }

  def always2[O,S,T](i1:Future[S],i2:Future[T])(f:(S,T)=>O) = {
    val tup = i1.zip(i2)
    tup.map(x => f(x._1,x._2))

  }






}
